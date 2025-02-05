package auth.module.config

import auth.module.Constants
import exception.handler.module.config.MessageKeys
import exception.handler.module.config.MessageResolver
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import service.config.module.utils.LoggerProvider

@Component
class AuthenticationEntryPoint(private val messageResolver: MessageResolver): ServerAuthenticationEntryPoint {

    private val logger = LoggerProvider.logger<AuthenticationEntryPoint>()

    override fun commence(exchange: ServerWebExchange?, ex: AuthenticationException?): Mono<Void> {
        val token = exchange?.request?.headers?.get(Constants.HEADER_AUTHORIZATION)
        if (token.isNullOrEmpty()  || !token[0].startsWith(Constants.BEARER)) {
            val locale = exchange?.localeContext?.locale
            throw CustomException(
                errorMessage = messageResolver.getErrorMessage(
                    MessageKeys.KEY_TOKEN_AUTHENTICATION_ERROR,
                    locale,
                    ex?.localizedMessage ?: "token is invalid"),
                httpStatus = HttpStatus.UNAUTHORIZED.value(),
                businessCode = ErrorCode.TOKEN_ERROR_401
            )
        } else {
            logger.warn("Error ( error: ${ex?.localizedMessage ?: "unknown error"} ) will be dispatched to security handler.")
            return Mono.empty()
        }
    }
}