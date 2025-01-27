package auth.module.dto

data class Token(
    val token: String,
    val expiresIn: Long,
    val refreshExpiresIn: Long,
    val refreshToken: String,
    val sessionState: String,
    val tokenType: String?,
    val scope: String?
)