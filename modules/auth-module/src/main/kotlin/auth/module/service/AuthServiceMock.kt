package auth.module.service

import org.springframework.stereotype.Service

@Service
class AuthServiceMock {

    suspend fun wellKnown(): String {
        return "done"
    }
}
