package auth.module.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("keycloak")
data class KeycloakProps(
    var serverUrl: String = "",
    var realm: String = "",
    var clientId: String = "",
    var clientSecret: String = ""
)
