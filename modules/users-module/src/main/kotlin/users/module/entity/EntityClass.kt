package users.module.entity

import org.springframework.data.annotation.Id
import users.module.utils.UsersServiceUtils.generateMonotonicULID

abstract class EntityClass {
    @Id
    val id: String = generateMonotonicULID()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        return id.isNotBlank() && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}