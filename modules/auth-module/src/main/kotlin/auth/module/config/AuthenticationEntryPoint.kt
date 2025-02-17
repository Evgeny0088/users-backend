package auth.module.config

import auth.module.Constants
import auth.module.utils.JwtTokenUtils.retrieveBearerTokenFromWebExchange
import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.exception.CustomException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import service.config.module.utils.LoggerProvider

@Component
class AuthenticationEntryPoint(private val messageResolver: ErrorsMessageResolver): ServerAuthenticationEntryPoint {

    private val logger = LoggerProvider.logger<AuthenticationEntryPoint>()

    override fun commence(exchange: ServerWebExchange?, ex: AuthenticationException?): Mono<Void> {
        val token = exchange?.let { retrieveBearerTokenFromWebExchange(it) }

        if (token.isNullOrEmpty()  || !token.startsWith(Constants.BEARER)) {
            throw CustomException.defaultUnauthorizedException(
                messageResolver,
                ex?.localizedMessage ?: "token is invalid"
            )
        } else {
            logger.warn("Error ( error: ${ex?.localizedMessage ?: "unknown error"} ) will be dispatched to security handler.")
            return Mono.empty()
        }
    }
}