package auth.module.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInfoDto(
    @JsonProperty("sub")
    val sub: String?,

    @JsonProperty("email_verified")
    val emailVerified: Boolean = false,

    @JsonProperty("name")
    val name: String?,

    @JsonProperty("preferred_username")
    val preferredName: String?,

    @JsonProperty("given_name")
    val givenName: String?,

    @JsonProperty("user-management-client-roles")
    val userRoles: List<String> = emptyList(),

    @JsonProperty("family_name")
    val familyName: String?,

    @JsonProperty("email")
    val email: String
    )
