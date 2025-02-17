package auth.module.dto

import exception.handler.module.dto.ValidatedDto

data class UserResponse(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String
): ValidatedDto()
