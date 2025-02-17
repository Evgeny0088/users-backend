package users.module.utils

import com.github.f4b6a3.ulid.UlidCreator

object UsersServiceUtils {

    fun generateMonotonicULID(): String {
        return UlidCreator.getMonotonicUlid().toUuid().toString()
    }

}