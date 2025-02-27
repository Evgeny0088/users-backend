package auth.module.service

import org.springframework.stereotype.Service

@Service
class AuthService {

    suspend fun wellKnown(): String {
        return "done"
    }
}
