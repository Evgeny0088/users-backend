package auth.module

object Constants {

    const val BEARER = "Bearer"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val CLIENT_KEYCLOAK_BEAN = "client-keycloak-bean"
    const val CLAIM_CLIENT_ROLES_KEY = "user-management-client-roles"
    const val CLAIM_PREFERRED_USERNAME = "preferred_username"
    const val KEYCLOAK_WEB_CLIENT = "keycloak-client"
    const val KEYCLOAK_WEB_CLIENT_CUSTOMIZER = "keycloak-client-customizer"


    val PERMITTED_ENDPOINTS = arrayOf(
        "/actuator/**",
        "/api/v1/auth/**",
        "/api/v1/employees/**"
    )
    val RESTRICTED_FROM_USER = arrayOf(
        "/api/v1/admin",
        "/api/v1/manager"
    )
}
