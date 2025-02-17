package users.module.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "department")
class Department(
    @Column("dep_name")
    var depName: String,

    @Column("created_at")
    var createdAt: LocalDate = LocalDate.now(),

    val employees: List<Employee> = emptyList()
): EntityClass()