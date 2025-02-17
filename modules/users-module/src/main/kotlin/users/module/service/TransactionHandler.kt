package users.module.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TransactionHandler {

    @Transactional(readOnly = true)
    suspend fun<T> inTransactionRead(block: suspend () -> T): T {
        return block.invoke()
    }

    @Transactional
    suspend fun<T> inTransaction(block: suspend () -> T): T {
        return block.invoke()
    }

}