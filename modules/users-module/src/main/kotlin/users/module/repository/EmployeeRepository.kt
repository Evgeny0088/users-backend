package users.module.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import users.module.entity.Employee


interface EmployeeRepository:  CoroutineCrudRepository<Employee, String> {

    fun findEmployeeByEmpName(name: String): Flow<Employee>

    fun findEmployeesByEmail(name: String, email: String): Flow<Employee>

}