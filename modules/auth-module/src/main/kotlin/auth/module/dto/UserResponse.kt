package auth.module.dto

data class UserResponse(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String
)
