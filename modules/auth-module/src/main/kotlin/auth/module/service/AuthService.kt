package auth.module.service

import auth.module.dto.*
import auth.module.mapper.TokenMapper
import auth.module.mapper.UserMapper
import auth.module.properties.KeycloakProps
import email.service.module.dto.EmailDto
import email.service.module.service.EmailService
import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.config.MessageKeys.KEY_REGISTRATION_ALREADY_DONE
import exception.handler.module.config.MessageKeys.KEY_REGISTRATION_ERROR
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import instrumentation.module.config.InstrumentationConfig.registerGauge
import instrumentation.module.config.InstrumentationConfig.registerTimer
import instrumentation.module.config.InstrumentationConfig.timeMetric
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import jakarta.validation.Valid
import kotlinx.coroutines.delay
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import service.config.module.utils.LoggerProvider.logger
import users.module.dto.DepartmentDto
import users.module.dto.EmployeeDto
import users.module.service.UsersService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@Service
class AuthService(
    private val keycloakService: KeycloakService,
    private val keycloakProps: KeycloakProps,
    private val userMapper: UserMapper,
    private val tokenMapper: TokenMapper,
    private val messageResolver: ErrorsMessageResolver,
    private val usersService: UsersService,
    private val emailService: EmailService,
    meterRegistry: MeterRegistry,
) {

    private val logger = logger<AuthService>()

    private var gaugeValue: AtomicInteger = AtomicInteger(0)
    private lateinit var timed: Timer

    init {
        registerGauge("gauge.random.value", gaugeValue, meterRegistry)
        timed = registerTimer("profile.timed", "measure time of profile request", meterRegistry)
    }

    suspend fun signUp(request: SignUpRequest): UserResponse {
        val realmResource = keycloakService.getRealmResource()
        val usersResource = realmResource.users()
        val client = realmResource.clients().findByClientId(keycloakProps.clientId)[0]
        val roles = realmResource.clients().get(client.id).roles().list()
        val role = assignRole(request.role, roles)
        val user = userMapper.toRepresentation(request)

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
                    errorMessage = messageResolver.getMessage(KEY_REGISTRATION_ALREADY_DONE),
                    httpStatus = 409,
                    businessCode = ErrorCode.USER_ALREADY_REGISTERED
                )
            } else {
                logger.error(errorMessage, response.status)
                throw CustomException(
                    errorMessage = messageResolver.getMessage(KEY_REGISTRATION_ERROR),
                    httpStatus = response.status,
                    businessCode = ErrorCode.USER_NOT_REGISTERED
                )
            }
        }

        return userMapper.toDto(savedUser)
    }

    suspend fun login(loginRequest: LoginRequest): TokenResponse {
        val username = loginRequest.username
        val accessToken = keycloakService.grandToken(username, loginRequest.password)
        logger.info("User (username: {} ) is authenticated.", username)
        return tokenMapper.toDto(accessToken)
    }

    suspend fun refreshToken(refreshRequest: RefreshRequest): TokenResponse {
        val accessToken = keycloakService.getRefreshedToken(refreshRequest)
        return tokenMapper.toDto(accessToken)
    }

    suspend fun logout(logoutRequest: LogoutRequest): BooleanRequest {
        val username = logoutRequest.username
        keycloakService.logoutUser(logoutRequest)
        logger.info("User ( username {} ) is logged out.", username)
        return BooleanRequest(true)
    }

    suspend fun deleteUser(userId: String): BooleanRequest {
        keycloakService.deleteUser(userId)
        logger.info("User ( id: {} ) is deleted.", userId)
        return BooleanRequest(true)
    }

    suspend fun saveTest(@Valid dto: EmployeeDto): EmployeeDto {
        return usersService.saveEmployee(dto)
    }

    suspend fun getDepartmentTest(name: String): DepartmentDto {
        return usersService.getDepartmentByName(name)
            ?: DepartmentDto("default", LocalDateTime.now())
    }

    // demo method only
    suspend fun profile(): String {
        var result = ""
        timeMetric(timed) {
            val random = Random.nextInt(0, 5000)
            logger.info("Endpoint /api/v1/profile is called! ...")
            gaugeValue.set(random)
            delay(random.toLong())
            emailService.sendEmail(EmailDto(
                "user-1",
                "test@email.com",
                "https://localhost:8080",
                LocalDate.now())
            )
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
}
