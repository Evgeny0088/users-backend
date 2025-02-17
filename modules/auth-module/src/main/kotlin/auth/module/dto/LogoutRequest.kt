package auth.module.dto

import exception.handler.module.dto.ValidatedDto
import jakarta.validation.constraints.NotBlank

data class LogoutRequest(
    @field:NotBlank
    val userId: String,
    @field:NotBlank
    val username: String
): ValidatedDto()
