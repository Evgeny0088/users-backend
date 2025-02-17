package auth.module.config

import auth.module.Constants.CLIENT_KEYCLOAK_BEAN
import auth.module.Constants.KEYCLOAK_WEB_CLIENT
import auth.module.Constants.KEYCLOAK_WEB_CLIENT_CUSTOMIZER
import auth.module.properties.KeycloakProps
import auth.module.utils.ServiceUtils
import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.config.MessageKeys
import exception.handler.module.exception.CustomException
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import jakarta.ws.rs.client.Client
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import service.config.module.utils.LoggerProvider
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebClientAutoConfiguration::class)
class ServiceConfig {

    private val logger = LoggerProvider.logger<ServiceConfig>()

    @Bean
    fun keycloakRolesConverter(): Converter<Jwt, Mono<AbstractAuthenticationToken>> = KeycloakRolesConverter()

    @Bean
    fun keycloakProperties() = KeycloakProps()

    @Bean
    fun restEasyClient(keycloakProperties: KeycloakProps): Client {
        return ResteasyClientBuilder
            .newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .sslContext(ServiceUtils.setupSSLContext(keycloakProperties))
            .build()
    }

    @Bean(CLIENT_KEYCLOAK_BEAN)
    fun keycloak(keycloakProperties: KeycloakProps, keyCloakClient: Client): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.serverUrl)
            .realm(keycloakProperties.realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(keycloakProperties.clientId)
            .clientSecret(keycloakProperties.clientSecret)
            .resteasyClient(keyCloakClient)
            .build()
    }

    @Bean
    fun exchangeFilterFunction(messageResolver: ErrorsMessageResolver): ExchangeFilterFunction {
        return ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
            logger.info("Request: {} {}", request.method(), request.url())
            next.exchange(request).flatMap { response: ClientResponse ->
                logger.info("Response: {}, {}", response.request().uri, response.statusCode());
                if (!response.statusCode().is2xxSuccessful && !response.statusCode().is3xxRedirection) {
                    if (response.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                        logger.error("Sessions not found for user, request unauthenticated: {}", request.url())
                        Mono.error(CustomException.defaultUnauthorizedException(messageResolver, "user sessions not found"))
                    } else {
                        logger.error("Error occurred during request: {}", request.url())
                        response.createException().flatMap {
                            val message = messageResolver.getMessage(MessageKeys.KEY_TOKEN_AUTHENTICATION_ERROR, it.message)
                            Mono.error(WebClientResponseException(response.statusCode().value(), message, null, null, null))
                        }
                    }
                } else Mono.just(response)
            }
        }
    }

    @Bean(KEYCLOAK_WEB_CLIENT_CUSTOMIZER)
    fun webClientCustomizer(
        exchangeFilterFunction: ExchangeFilterFunction
    ): WebClientCustomizer {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected { conn: Connection ->
                conn
                    .addHandlerLast(ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS))
            }
        return WebClientCustomizer { builder-> builder
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(exchangeFilterFunction)
        }
    }

    @Bean(KEYCLOAK_WEB_CLIENT)
    fun webclient(
        builder: WebClient.Builder,
        exchangeFilterFunction: ExchangeFilterFunction,
        keycloakProperties: KeycloakProps): WebClient {
        return builder
            .baseUrl(keycloakProperties.serverUrl)
            .build()
    }
}