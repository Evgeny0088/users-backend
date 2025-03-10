package service.config.module.logging

import com.fasterxml.jackson.databind.ObjectMapper
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Mono
import service.config.module.utils.LoggerProvider
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

class ResponseDecorator(response: ServerHttpResponse, private val request: ServerHttpRequest, private val mapper: ObjectMapper): ServerHttpResponseDecorator(response) {

    private val log = LoggerProvider.logger<ResponseDecorator>()

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        val logResponse = LogResponse()
            .also {
                it.url = request.uri.toString()
                it.statusCode = super.getDelegate().statusCode?.value()
            }
        val buffer: Mono<DataBuffer> = Mono.from(body)
        return super.writeWith(
            buffer
                .doOnNext { dataBuffer: DataBuffer ->
                    try {
                        ByteArrayOutputStream().use { byteArrayOutputStream ->
                            Channels.newChannel(byteArrayOutputStream)
                                .write(dataBuffer.readableByteBuffers().next())
                            logResponse.body = byteArrayOutputStream.toByteArray().toString(StandardCharsets.UTF_8)
                            val requestBody = mapper.writeValueAsString(logResponse)
                            log.info("Output response:\n${requestBody} ")
                        }
                    }
                    catch (e: Exception) {
                        log.error("Response logging error: ${e.message}")
                    } }
        )
    }
}
