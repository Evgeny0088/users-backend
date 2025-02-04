package auth.module.config

import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class AccessDeniedHandler: ServerAccessDeniedHandler {

    override fun handle(exchange: ServerWebExchange, accessDeniedException: AccessDeniedException?): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        response.setStatusCode(HttpStatus.FORBIDDEN)
        val url = exchange.request.uri.toURL().toString()
        throw CustomException(
            errorMessage = "User access is forbidden to resource: ".plus(url),
            httpStatus = HttpStatus.FORBIDDEN.value(),
            businessCode = ErrorCode.USER_IS_FORBIDDEN
        )
    }
}