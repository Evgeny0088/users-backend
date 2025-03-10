package service.config.module.logging

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.server.CoWebFilter
import org.springframework.web.server.CoWebFilterChain
import org.springframework.web.server.ServerWebExchange

@Component
class LoggingFilter(val mapper: ObjectMapper) : CoWebFilter() {

    override suspend fun filter(exchange: ServerWebExchange, chain: CoWebFilterChain) {

        val requestDecorator = RequestDecorator(exchange.request)
        val responseDecorator = ResponseDecorator(exchange.response, exchange.request, mapper)
        return chain.filter(
            exchange.mutate()
                .request(requestDecorator)
                .response(responseDecorator)
                .build())
    }
}