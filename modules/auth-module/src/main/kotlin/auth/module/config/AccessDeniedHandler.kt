package auth.module.config

import auth.module.utils.JwtTokenUtils
import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.config.MessageKeys
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AccessDeniedHandler(private val messageResolver: ErrorsMessageResolver): ServerAccessDeniedHandler {

    override fun handle(exchange: ServerWebExchange, accessDeniedException: AccessDeniedException?): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.setStatusCode(HttpStatus.FORBIDDEN)
        val url = exchange.request.uri.path
        val locale = exchange.localeContext.locale
        val token = JwtTokenUtils.retrieveTokenFromWebExchange(exchange)
        val jwt = JwtTokenUtils.decodeToken(token)
        val username = JwtTokenUtils.retrieveUsernameFromTokenClaim(jwt)
        val userRole = JwtTokenUtils.retrieveUserRoleFromTokenClaim(jwt)
        throw CustomException(
            errorMessage = messageResolver.getMessage(MessageKeys.KEY_USER_NOT_AUTHORIZED, username, userRole, url),
            httpStatus = HttpStatus.FORBIDDEN.value(),
            businessCode = ErrorCode.USER_IS_FORBIDDEN
        )
    }
}