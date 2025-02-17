package users.module.service

import kotlinx.coroutines.flow.firstOrNull
import org.springframework.stereotype.Service
import service.config.module.utils.LoggerProvider
import users.module.dto.DepartmentDto
import users.module.dto.EmployeeDto
import users.module.mapper.DepartmentMapper
import users.module.mapper.EmployeeMapper
import users.module.repository.DepartmentRepository
import users.module.repository.EmployeeRepository

@Service
class UsersService(
    private val transactionHandler: TransactionHandler,
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository,
    private val employeeMapper: EmployeeMapper,
    private val departmentMapper: DepartmentMapper
) {

    private val logger = LoggerProvider.logger<UsersService>()

    suspend fun testMethod(): String {
        return "done"
    }

    suspend fun saveEmployee(dto: EmployeeDto): EmployeeDto {
        return transactionHandler.inTransaction {
            logger.info("Saving user ( name: {}, email: {})", dto.empName, dto.email)
            val employee = employeeRepository.save(employeeMapper.toEntity(dto))
            employeeMapper.toDto(employee)
        }
    }

    suspend fun deleteEmployee(id: String): String {
        return transactionHandler.inTransaction {
            logger.info("Deleting user ( id: {} )", id)
            employeeRepository.deleteById(id)
            return@inTransaction id
        }
    }

    suspend fun getDepartmentByName(name: String): DepartmentDto? {
        return transactionHandler.inTransactionRead {
            logger.info("Getting department: {}", name)
            val department = departmentRepository.findDepartmentByNameFetch(name).firstOrNull()
            department?.let { departmentMapper.toDto(it) }
        }
    }
}