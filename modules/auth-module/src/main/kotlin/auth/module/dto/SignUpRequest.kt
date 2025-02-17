package auth.module.dto

import exception.handler.module.dto.ValidatedDto
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:NotBlank
    @field:Size(min = 5, max = 10)
    val username: String,

    @field: NotBlank
    @field:Size(min = 5, max = 100)
    val firstName: String,

    @field: NotBlank
    @field:Size(min = 5, max = 100)
    val lastName: String,

    @field: NotBlank
    @field:Size(min = 5, max = 200)
    val password: String,

    @field:Email
    val email: String,

    val isEnabled: Boolean = false,
    val role: String? = null
): ValidatedDto()
