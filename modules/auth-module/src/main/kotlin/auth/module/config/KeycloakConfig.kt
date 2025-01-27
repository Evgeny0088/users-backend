package auth.module.config

import auth.module.config.Constants.CLIENT_KEYCLOAK_BEAN
import auth.module.properties.KeycloakProps
import auth.module.utils.ServiceUtils
import jakarta.ws.rs.client.Client
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration(proxyBeanMethods = false)
class KeycloakConfig {

    @Bean
    fun keycloakProperties() = KeycloakProps()

    @Bean
    fun restEasyClient(keycloakProperties: KeycloakProps): Client {
        return ResteasyClientBuilder
            .newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .sslContext(ServiceUtils.setupSSLContext(keycloakProperties))
            .build()
    }

    @Bean(CLIENT_KEYCLOAK_BEAN)
    fun keycloak(keycloakProperties: KeycloakProps, keyCloakClient: Client): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.serverUrl)
            .realm(keycloakProperties.realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(keycloakProperties.clientId)
            .clientSecret(keycloakProperties.clientSecret)
            .resteasyClient(keyCloakClient)
            .build()
    }
}