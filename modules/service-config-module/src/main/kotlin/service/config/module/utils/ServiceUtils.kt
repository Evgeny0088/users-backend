package service.config.module.utils

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.reactive.function.server.ServerRequest
import java.util.*

object ServiceUtils {

    fun retrieveLocale(request: ServerRequest): Locale {
        return request.headers().acceptLanguage()
            .takeIf { it.isNotEmpty() }
            ?.let { Locale(it[0].range) }
            ?: Locale("en")
    }

    fun retrieveLocale(request: ServerHttpRequest?): Locale {
        return request?.headers?.acceptLanguage
            ?.let { Locale(it[0].range) }
            ?: Locale("en")
    }
}