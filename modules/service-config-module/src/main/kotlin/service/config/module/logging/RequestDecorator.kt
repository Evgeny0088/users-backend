package service.config.module.logging

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import service.config.module.utils.LoggerProvider
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

class RequestDecorator(request: ServerHttpRequest): ServerHttpRequestDecorator(request) {

    private val log = LoggerProvider.logger<RequestDecorator>()
    private var body: Flux<DataBuffer>?
    private val logRequest: LogRequest = LogRequest()
        .also {
            it.url = super.getURI().toString()
            it.params = super.getQueryParams().toSingleValueMap()
        }

    init {
        log.info("Incoming request: ${logRequest.url}, ${logRequest.params}")
        if (request.headers.contentLength < 1000 && request.headers.contentType == MediaType.APPLICATION_JSON) {
            val outputStream = ByteArrayOutputStream()
            body = super.getBody()
                .doOnNext { buffer: DataBuffer ->
                    runCatching {
                        outputStream.use {
                            Channels.newChannel(it)
                                .write(buffer.readableByteBuffers().next())
                        }
                    }
                        .onFailure { log.error("Cannot read input body!!!") }
                }
                .doFinally {
                    logRequest.body = outputStream.toByteArray().toString(StandardCharsets.UTF_8)
                    log.info("Incoming request body:\n${logRequest.body}")
                }
        } else {
            log.info("Incoming request body type: {}, content length: {}",
                request.headers.contentType, request.headers.contentLength)
            body = super.getBody()
        }
    }

    override fun getBody(): Flux<DataBuffer> {
        return body!!
    }
}
