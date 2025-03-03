package users.module.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "employee")
class Employee (

    @Column("emp_name")
    var empName: String,

    @Column("last_name")
    var lastName: String,

    @Column("email")
    var email: String,

    @Column("birthday")
    var birthday: LocalDate,

    @Column("created_at")
    var createdAt: LocalDateTime,

    @Column("salary")
    var salary: Int? = 0,

    @Column("department_id")
    var departmentId: String
) : EntityClass()