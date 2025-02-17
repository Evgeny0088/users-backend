package users.module.mapper

import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import users.module.dto.DepartmentDto
import users.module.entity.Department

@Mapper(
    componentModel = "spring",
    uses = [EmployeeMapper::class],
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class DepartmentMapper: GenericEntityMapper<Department, DepartmentDto>