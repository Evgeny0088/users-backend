package users.module.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import users.module.entity.Department

interface DepartmentRepository:  CoroutineCrudRepository<Department, String> {

    fun findDepartmentByDepName(name: String): Flow<Department>

    @Query(
        """
        select d from Department d join fetch d.employees
        where d.dep_name=:depName
        """
    )
    fun findDepartmentByNameFetch(depName: String): Flow<Department>

}