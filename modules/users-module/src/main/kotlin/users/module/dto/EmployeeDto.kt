package users.module.dto

import exception.handler.module.dto.ValidatedDto
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime

data class EmployeeDto (
    @field:NotBlank
    val empName: String?,
    @field: NotBlank(message = "lastName field must be specified.")
    val lastName: String?,
    @field:Email(message = "email must satisfy general email pattern.")
    val email: String?,
    @field:NotNull(message = "birthday field must be specified.")
    val birthday: LocalDate?,
    @field: NotNull(message = "departmentId field must be specified.")
    val departmentId: String?,
    val salary: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
): ValidatedDto()
