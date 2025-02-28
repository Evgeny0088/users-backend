package auth.module.dto

import exception.handler.module.dto.ValidatedDto
import jakarta.validation.constraints.NotBlank

data class RefreshRequest(
    @field:NotBlank
    val username: String?,
    val refreshToken: String?
): ValidatedDto()
