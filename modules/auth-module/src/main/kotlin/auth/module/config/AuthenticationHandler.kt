package auth.module.config

import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import reactor.core.publisher.Mono

class AuthenticationHandler: ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange?, exception: AuthenticationException?): Mono<Void> {
        val url = webFilterExchange?.exchange?.request?.uri?.toURL().toString()
        throw CustomException(
            errorMessage = "User is not authorized to resource: ".plus(url),
            httpStatus = HttpStatus.UNAUTHORIZED.value(),
            businessCode = ErrorCode.USER_IS_FORBIDDEN
        )
    }
}