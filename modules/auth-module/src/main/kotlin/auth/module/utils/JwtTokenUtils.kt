package auth.module.utils

import auth.module.Constants
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import org.springframework.web.server.ServerWebExchange

object JwtTokenUtils {

    fun retrieveTokenFromWebExchange(exchange: ServerWebExchange): String? {
        return exchange.request.headers[Constants.HEADER_AUTHORIZATION]?.let { it[0].substring(7) }
    }

    fun decodeToken(token: String?): JWT? {
        return runCatching { JWTParser.parse(token) }.getOrNull()
    }

    fun retrieveUsernameFromTokenClaim(jwt: JWT?): String {
        return jwt?.jwtClaimsSet?.claims?.get(Constants.CLAIM_PREFERRED_USERNAME)?.toString() ?: "unknown username"
    }

    fun retrieveUserRoleFromTokenClaim(jwt: JWT?): String {
        val roles = jwt?.jwtClaimsSet?.claims?.get(Constants.CLAIM_CLIENT_ROLES_KEY)
        return if (roles != null && roles is List<*>) {
            roles.joinToString(separator = ",")
        } else ""
    }
}