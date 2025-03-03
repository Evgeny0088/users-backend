package auth.module.service

import org.springframework.stereotype.Service
import users.module.dto.EmployeeDto

@Service
class AuthServiceMock {

    suspend fun wellKnown(): String {
        return "done"
    }

    suspend fun testProfile(emp: EmployeeDto): String {
        return "test profile: ${emp.empName}"
    }
}
