package users.module.dto

import exception.handler.module.dto.ValidatedDto
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class DepartmentDto(
    @field:NotBlank
    val name: String?,
    @field:NotBlank
    val createdAt: LocalDateTime?,
    val employees: List<EmployeeDto> = emptyList()
): ValidatedDto()
