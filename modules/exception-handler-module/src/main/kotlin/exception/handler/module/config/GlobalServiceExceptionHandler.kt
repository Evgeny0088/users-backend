package exception.handler.module.config

import exception.handler.module.exception.CustomException
import exception.handler.module.exception.ValidationException
import exception.handler.module.mapper.ExceptionMapper
import jakarta.ws.rs.ClientErrorException
import jakarta.ws.rs.NotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import service.config.module.utils.LoggerProvider

@Component
@Order(-2)
class GlobalServiceExceptionHandler(
    errorAttributes: ErrorAttributes?,
    resources: WebProperties,
    applicationContext: ApplicationContext,
    codecs: ServerCodecConfigurer,
    exceptionMapper: ExceptionMapper
) : AbstractErrorWebExceptionHandler(errorAttributes, resources.resources, applicationContext) {

    @Value("\${logging.stack-trace.print}")
    private val print: Boolean = true

    @Value("\${logging.stack-trace.lines-max}")
    private val maxLines: Int = 10

    private lateinit var exceptionMapper: ExceptionMapper

    private val logger = LoggerProvider.logger<GlobalServiceExceptionHandler>()

    init {
        this.setMessageReaders(codecs.readers)
        this.setMessageWriters(codecs.writers)
        this.exceptionMapper = exceptionMapper
    }

    override fun logError(request: ServerRequest?, response: ServerResponse?, throwable: Throwable?) {
        if (print) {
            throwable?.let {
                val trace = it.stackTraceToString().lines()
                logger.error(
                    trace.subList(0, trace.size.coerceAtMost(maxLines))
                        .joinToString(separator = "\n")
                        .plus("\n< ... >"))
            }
        }
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        return when (val throwable = getError(request)) {
            is ValidationException -> {
                ServerResponse
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionMapper.mapFromWebArgumentsException(throwable)))
            }
            is NotFoundException -> {
                ServerResponse
                    .status(throwable.response.status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionMapper.mapFromNoFoundException(throwable)))
            }
            is ClientErrorException -> {
                ServerResponse
                    .status(throwable.response.status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionMapper.mapFromClientErrorException(throwable)))
            }
            is WebClientResponseException -> {
                ServerResponse
                    .status(throwable.statusCode.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionMapper.mapFromWebClientResponseException(throwable)))
            }
            is CustomException -> {
                ServerResponse
                    .status(throwable.httpStatus)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionMapper.mapFromCustomException(throwable)))
            }
            else -> {
                ServerResponse
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(exceptionMapper.mapFromException(throwable)))
            }
        }
    }
}
