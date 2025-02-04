package auth.module.config

import auth.module.Constants.PERMITTED_ENDPOINTS
import auth.module.Constants.RESTRICTED_FROM_USER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: ServerHttpSecurity,
        keycloakRolesConverter: Converter<Jwt, Mono<AbstractAuthenticationToken>>): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable()}
            .authorizeExchange {
                    c -> c
                        .pathMatchers(*PERMITTED_ENDPOINTS).permitAll()
                        .pathMatchers(*RESTRICTED_FROM_USER).hasAnyRole("ADMIN", "MANAGER")
                        .anyExchange()
                        .authenticated()
            }
            .oauth2ResourceServer { auth ->
                auth
                    .jwt { it.jwtAuthenticationConverter(keycloakRolesConverter) }

                    .authenticationFailureHandler { exchange, exception ->
                        AuthenticationHandler().onAuthenticationFailure(exchange, exception)}

                    .accessDeniedHandler { exchange, denied ->
                        AccessDeniedHandler().handle(exchange, denied)}
            }
            .addFilterAfter(JwtFilter(), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE)
            .build()
    }
}