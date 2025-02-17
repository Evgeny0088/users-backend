package auth.module.config

import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.exception.CustomException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationHandler(
    private val messageResolver: ErrorsMessageResolver): ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange?, ex: AuthenticationException?): Mono<Void> {
        throw CustomException.defaultUnauthorizedException(
            messageResolver,
            args = arrayOf(ex?.localizedMessage ?: "token is invalid")
        )
    }
}