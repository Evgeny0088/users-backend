package auth.module.service

import auth.module.Constants.CLIENT_KEYCLOAK_BEAN
import auth.module.dto.*
import auth.module.mapper.TokenMapper
import auth.module.mapper.UserMapper
import auth.module.properties.KeycloakProps
import exception.handler.module.config.MessageKeys.KEY_REGISTRATION_ALREADY_DONE
import exception.handler.module.config.MessageKeys.KEY_REGISTRATION_ERROR
import exception.handler.module.config.MessageResolver
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import instrumentation.module.config.InstrumentationConfig.registerGauge
import instrumentation.module.config.InstrumentationConfig.registerTimer
import instrumentation.module.config.InstrumentationConfig.timeMetric
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import jakarta.ws.rs.client.Client
import kotlinx.coroutines.delay
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import service.config.module.utils.LoggerProvider.logger
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@Service
class KeycloakService(
    @Qualifier(CLIENT_KEYCLOAK_BEAN)
    private val keycloak: Keycloak,
    private val keycloakClient: Client,
    private val keycloakProps: KeycloakProps,
    private val userMapper: UserMapper,
    private val tokenMapper: TokenMapper,
    private val messageResolver: MessageResolver,
    meterRegistry: MeterRegistry,
) {

    private var gaugeValue: AtomicInteger = AtomicInteger(0)
    private lateinit var timed: Timer

    init {
        registerGauge("gauge.random.value", gaugeValue, meterRegistry)
        timed = registerTimer("profile.timed", "measure time of profile request", meterRegistry)
    }

    private val logger = logger<KeycloakService>()

    suspend fun signUp(request: SignUpRequest, locale: Locale): UserResponse {
        val realmResource = keycloak.realm(keycloakProps.realm)
        val usersResource = realmResource.users()
        val client = realmResource.clients().findByClientId(keycloakProps.clientId)[0]
        val roles = realmResource.clients().get(client.id).roles().list()
        val role = assignRole(request.role, roles)
        val user = userMapper.toRepresentation(request)

        usersResource.get("").userSessions

        var savedUser = UserRepresentation()

        usersResource.create(user).use { response ->
            val errorMessage = "User cannot be registered: {}"
            if (response.status == 201) {
                savedUser = usersResource.searchByEmail(request.email, true)[0]
                val savedResource: UserResource = usersResource.get(savedUser.id)
                savedResource.roles().clientLevel(client.id).add(listOf(role))
                logger.info("User ( id: {}, username: {} ) is registered", savedUser.id, savedUser.username)
            } else if (response.status == 409 && response.statusInfo.reasonPhrase == "Conflict") {
                logger.error(errorMessage, response.status)
                throw CustomException(
                    errorMessage = messageResolver.getErrorMessage(KEY_REGISTRATION_ALREADY_DONE, locale),
                    httpStatus = 409,
                    businessCode = ErrorCode.USER_ALREADY_REGISTERED
                )
            } else {
                logger.error(errorMessage, response.status)
                throw CustomException(
                    errorMessage = messageResolver.getErrorMessage(KEY_REGISTRATION_ERROR, locale),
                    httpStatus = response.status,
                    businessCode = ErrorCode.USER_NOT_REGISTERED
                )
            }
        }

        return userMapper.toDto(savedUser)
    }

    suspend fun login(loginRequest: LoginRequest, locale: Locale): Token {
        val username = loginRequest.username
        val accessToken = loginKeycloakClient(username, loginRequest.password).tokenManager().grantToken()
        logger.info("User (username: {} ) is authenticated.", username)
        return tokenMapper.toDto(accessToken)
    }

    suspend fun logout(logoutRequest: LogoutRequest): BooleanRequest {
        val username = logoutRequest.username
        keycloak.realm(keycloakProps.realm).users().get(logoutRequest.userId).logout()
        logger.info("User ( username {} ) is logged out.", username)
        return BooleanRequest(true)
    }

    suspend fun deleteUser(userId: String, locale: Locale): BooleanRequest {
        keycloak.realm(keycloakProps.realm).users().get(userId).remove()
        logger.info("User ( id: {} ) is deleted.", userId)
        return BooleanRequest(true)
    }

    // demo method only
    suspend fun profile(): String {
        var result = ""
        timeMetric(timed) {
            val random = Random.nextInt(0, 5000)
            logger.info("Endpoint /api/v1/profile is called! ...")
            gaugeValue.set(random)
            delay(random.toLong())
            if (random % 3 == 0) {
                throw CustomException("Profile error", 400, ErrorCode.INPUT_BAD_REQUEST)
            }
            result=  "done"
        }
        return result
    }

    private fun assignRole(role: String?, roles: List<RoleRepresentation>): RoleRepresentation {
        return role?.let { retrieveRole(it, roles) ?: defaultRole(roles) } ?: defaultRole(roles)
    }

    private fun retrieveRole(inputRole: String, roles: List<RoleRepresentation>): RoleRepresentation? {
        return roles.find { r-> r.name == inputRole }
    }

    private fun defaultRole(roles: List<RoleRepresentation>): RoleRepresentation {
        return roles.find { r-> r.name == "ROLE_USER" }!!
    }

    private fun loginKeycloakClient(username: String, password: String): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProps.serverUrl)
            .realm(keycloakProps.realm)
            .clientId(keycloakProps.clientId)
            .clientSecret(keycloakProps.clientSecret)
            .grantType(OAuth2Constants.PASSWORD)
            .username(username)
            .password(password)
            .resteasyClient(keycloakClient)
            .build()
    }
}