package auth.module

object Constants {

    const val BEARER = "Bearer"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val CLIENT_KEYCLOAK_BEAN = "client-keycloak-bean"
    const val CLAIM_CLIENT_ROLES_KEY = "user-management-client-roles"
    const val CLAIM_PREFERRED_USERNAME = "preferred_username"

    val PERMITTED_ENDPOINTS = arrayOf(
        "/actuator/**",
        "/api/v1/auth/**"
    )
    val RESTRICTED_FROM_USER = arrayOf(
        "/api/v1/admin",
        "/api/v1/manager"
    )
}