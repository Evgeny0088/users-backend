package auth.module.config

import auth.module.Constants.HEADER_AUTHORIZATION
import auth.module.Constants.KEYCLOAK_WEB_CLIENT
import auth.module.dto.UserInfoDto
import auth.module.properties.KeycloakProps
import auth.module.utils.JwtTokenUtils.retrieveBearerTokenFromWebExchange
import auth.module.utils.ServiceUtils.retryPolicyWebClient
import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.exception.CustomException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import service.config.module.utils.LoggerProvider

class UserSessionVerificationFilter(
    @Qualifier(KEYCLOAK_WEB_CLIENT)private val webClient: WebClient,
    private val keycloakProps: KeycloakProps,
    private val messageResolver: ErrorsMessageResolver): WebFilter {

    private val logger = LoggerProvider.logger<UserSessionVerificationFilter>()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = retrieveBearerTokenFromWebExchange(exchange)
        return token?.let {
            webClient
                .get()
                .uri("${keycloakProps.serverUrl}/realms/${keycloakProps.realm}/protocol/openid-connect/userinfo")
                .header(HEADER_AUTHORIZATION, token)
                .exchangeToMono { resp -> resp.bodyToMono<UserInfoDto>() }
                .retryWhen(retryPolicyWebClient())
                .doOnNext {
                    if (!it.emailVerified) {
                        throw CustomException.defaultEmailVerificationException(messageResolver, it.email)
                    }
                    logger.info("Found active sessions for user: {}, request authenticated.", it.preferredName)
                }
                .then(chain.filter(exchange))
        } ?: chain.filter(exchange)
    }
}
