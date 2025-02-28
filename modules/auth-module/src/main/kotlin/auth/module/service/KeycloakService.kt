package auth.module.service

import auth.module.Constants
import auth.module.dto.LogoutRequest
import auth.module.dto.RefreshRequest
import auth.module.properties.KeycloakProps
import auth.module.utils.ServiceUtils
import com.nimbusds.oauth2.sdk.GrantType
import kotlinx.coroutines.reactor.awaitSingle
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.AccessTokenResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import service.config.module.utils.LoggerProvider

@Service
class KeycloakService(
    @Qualifier(Constants.CLIENT_KEYCLOAK_BEAN)
    private val keycloakClient: Keycloak,
    @Qualifier(Constants.CLIENT_KEYCLOAK_USER_BUILDER)
    private val keycloakUserBuilder: KeycloakBuilder,
    @Qualifier(Constants.KEYCLOAK_WEB_CLIENT)
    private val keycloakWebClient: WebClient,
    private val keycloakProps: KeycloakProps
) {

    private val logger = LoggerProvider.logger<AuthService>()

    fun getRealmResource(): RealmResource {
        return keycloakClient.realm(keycloakProps.realm)
    }

    fun grandToken(username: String, password: String): AccessTokenResponse {
        return loginKeycloakClient(username, password).tokenManager().grantToken()
    }

    suspend fun getRefreshedToken(refreshRequest: RefreshRequest): AccessTokenResponse {
        return keycloakWebClient
            .method(HttpMethod.POST)
            .uri("${keycloakProps.serverUrl}/realms/${keycloakProps.realm}/protocol/openid-connect/token")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(BodyInserters.fromFormData(buildMultiValueMap(keycloakProps, refreshRequest)))
            .exchangeToMono {resp-> resp.bodyToMono<AccessTokenResponse>() }
            .retryWhen(ServiceUtils.retryPolicyWebClient())
            .doOnNext {
                logger.info("refresh token received: $it")
                logger.info("User (username: {} ) is re-authenticated.", refreshRequest.username)
            }
            .awaitSingle()
    }

    fun logoutUser(logoutRequest: LogoutRequest) {
        keycloakClient.realm(keycloakProps.realm).users().get(logoutRequest.userId).logout()
    }

    fun deleteUser(userId: String) {
        keycloakClient.realm(keycloakProps.realm).users().get(userId).remove()
    }

    private fun loginKeycloakClient(username: String, password: String): Keycloak {
        return keycloakUserBuilder
            .scope("openid")
            .username(username)
            .password(password)
            .build()
    }

    private fun buildMultiValueMap(keycloakProps: KeycloakProps, request: RefreshRequest): MultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        map.add("client_id", keycloakProps.clientId)
        map.add("client_secret", keycloakProps.clientSecret)
        map.add("grant_type", GrantType.REFRESH_TOKEN.value)
        map.add("refresh_token", request.refreshToken)
        return map
    }
}