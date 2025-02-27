package auth.module.config

import auth.module.Constants
import kotlinx.coroutines.reactor.mono
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import reactor.core.publisher.Mono

class KeycloakRolesConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken> {
        return mono {
            JwtAuthenticationToken(source, extractResourceRoles(source))
        }
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority?> {
        val roles = jwt.getClaimAsStringList(Constants.CLAIM_CLIENT_ROLES_KEY).orEmpty()
        return roles
            .map { role: String ->
                SimpleGrantedAuthority(role)
            }.toSet()
    }
}
