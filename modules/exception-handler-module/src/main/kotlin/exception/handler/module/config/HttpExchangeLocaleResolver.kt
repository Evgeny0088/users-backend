package exception.handler.module.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.adapter.HttpWebHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import service.config.module.utils.ServiceUtils.retrieveLocale

@Configuration
class HttpExchangeLocaleResolver {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    fun httpHandler(applicationContext: ApplicationContext?): HttpHandler {
        val delegate: HttpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext!!).build()
        return object : HttpWebHandlerAdapter((delegate as HttpWebHandlerAdapter)) {
            override fun createExchange(request: ServerHttpRequest, response: ServerHttpResponse): ServerWebExchange {
                val serverWebExchange = super.createExchange(request, response)
                val locale = retrieveLocale(request)
                LocaleContextHolder.setLocaleContext { locale }
                return serverWebExchange
            }
        }
    }
}