package auth.module.utils

import auth.module.Constants.CLAIM_CLIENT_ROLES_KEY
import auth.module.Constants.CLAIM_PREFERRED_USERNAME
import auth.module.Constants.HEADER_AUTHORIZATION
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import org.springframework.web.server.ServerWebExchange

object JwtTokenUtils {

    fun retrieveBearerTokenFromWebExchange(exchange: ServerWebExchange): String? {
        return exchange.request.headers[HEADER_AUTHORIZATION]?.firstOrNull()
    }

    fun retrieveTokenFromWebExchange(exchange: ServerWebExchange): String? {
        return exchange.request.headers[HEADER_AUTHORIZATION]?.let { it[0].substring(7) }
    }

    fun decodeToken(token: String?): JWT? {
        return runCatching { JWTParser.parse(token) }.getOrNull()
    }

    fun retrieveUsernameFromTokenClaim(jwt: JWT?): String {
        return jwt?.jwtClaimsSet?.claims?.get(CLAIM_PREFERRED_USERNAME)?.toString() ?: "unknown username"
    }

    fun retrieveUserRoleFromTokenClaim(jwt: JWT?): String {
        val roles = jwt?.jwtClaimsSet?.claims?.get(CLAIM_CLIENT_ROLES_KEY)
        return if (roles != null && roles is List<*>) {
            roles.joinToString(separator = ",")
        } else ""
    }
}