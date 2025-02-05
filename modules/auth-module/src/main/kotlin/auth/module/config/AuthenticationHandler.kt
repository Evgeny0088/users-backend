package auth.module.config

import exception.handler.module.config.MessageKeys
import exception.handler.module.config.MessageResolver
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationHandler(private val messageResolver: MessageResolver): ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange?, exception: AuthenticationException?): Mono<Void> {
        val locale = webFilterExchange?.exchange?.localeContext?.locale
        throw CustomException(
            errorMessage = messageResolver.getErrorMessage(
                MessageKeys.KEY_TOKEN_AUTHENTICATION_ERROR,
                locale,
                exception?.localizedMessage ?: "token is invalid"),
            httpStatus = HttpStatus.UNAUTHORIZED.value(),
            businessCode = ErrorCode.TOKEN_ERROR_401
        )
    }
}