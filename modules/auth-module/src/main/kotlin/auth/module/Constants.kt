package auth.module

object Constants {

    const val CLIENT_KEYCLOAK_BEAN = "client-keycloak-bean"

    const val CLIENT_ROLES_KEY = "user-management-client-roles"

    val PERMITTED_ENDPOINTS = arrayOf(
        "/actuator/**",
        "/api/v1/auth/**"
    )
    val RESTRICTED_FROM_USER = arrayOf(
        "/api/v1/admin",
        "/api/v1/manager"
    )
}