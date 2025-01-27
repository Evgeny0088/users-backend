//package users.backend.modules.auth.converters
//
//import org.springframework.core.convert.converter.Converter
//import org.springframework.security.authentication.AbstractAuthenticationToken
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.oauth2.jwt.Jwt
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
//import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter
//import org.springframework.stereotype.Component
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//import users.backend.modules.auth.config.Constants.REALM_ACCESS
//import users.backend.modules.auth.config.Constants.ROLES
//import users.backend.modules.auth.config.Constants.ROLE_PREFIX
//import java.util.stream.Collectors.toSet
//import java.util.stream.Stream
//
//@Component
//class KeycloakJwtConverter(
//    jwtGrantedAuthoritiesConverter: Converter<Jwt?, Collection<GrantedAuthority?>?>?)
//    : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
//
//    private var jwtGrantedAuthoritiesConverter: Converter<Jwt, Flux<GrantedAuthority>>? = null
//
//    init {
//        this.jwtGrantedAuthoritiesConverter =
//            ReactiveJwtGrantedAuthoritiesConverterAdapter(jwtGrantedAuthoritiesConverter)
//    }
//
//    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken>? {
//        return jwtGrantedAuthoritiesConverter
//            ?.convert(source)
//            ?.collectList()
//            ?.map { authorities->
//                JwtAuthenticationToken(
//                    source,
//                    authorities,
//                    Stream.concat(authorities.stream(), extractResourceRoles(source).stream()).collect(toSet())
//                )
//            }
//    }
//
//        fun convertg(source: Jwt): AbstractAuthenticationToken {
//
//            return JwtAuthenticationToken(
//                source,
//                JwtGrantedAuthoritiesConverter().convert(source)?.stream()?.let {
//                    Stream.concat(
//                        it,
//                        extractResourceRoles(source).stream()
//                    ).collect(toSet())
//                }
//            )
//        }
//
//    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority?> {
//        val roles = jwt.getClaimAsMap(REALM_ACCESS)[ROLES] as List<String>
//        return roles
//            .filter { it.startsWith(ROLE_PREFIX) }
//            .map { role: String ->
//                SimpleGrantedAuthority(role)
//            }.toSet()
//    }
//}