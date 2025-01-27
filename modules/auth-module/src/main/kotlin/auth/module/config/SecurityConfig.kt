package auth.module.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable()}
            .authorizeExchange {
                    c ->
                c.pathMatchers(
                    "/actuator/**",
                    "/api/v1/auth/**",
                ).permitAll()
                    .anyExchange().authenticated()
            }
            .oauth2ResourceServer { auth ->
                auth.jwt { Customizer.withDefaults<JwtAuthenticationToken>()}
            }
            .build()
    }
}