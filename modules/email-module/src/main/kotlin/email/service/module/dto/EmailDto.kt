package email.service.module.dto

import java.time.LocalDate

data class EmailDto(
    val username: String,
    val email: String,
    val link: String,
    val registeredAt: LocalDate
)
