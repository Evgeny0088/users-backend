package auth.module.controller

import auth.module.dto.LoginRequest
import auth.module.dto.LogoutRequest
import auth.module.dto.SignUpRequest
import auth.module.service.AuthService
import auth.module.service.KeycloakService
import auth.module.utils.ServiceUtils.retrieveLocale
import auth.module.validator.LoginRequestValidator
import auth.module.validator.LogoutRequestValidator
import auth.module.validator.SignRequestValidator
import exception.handler.module.config.MessageKeys.KEY_REQUEST_BODY
import exception.handler.module.config.MessageResolver
import exception.handler.module.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.util.Locale

@Service
class AuthHandler(
    private val keycloakService: KeycloakService,
    private val authService: AuthService,
    private val messageResolver: MessageResolver,
    private val signValidator: SignRequestValidator,
    private val loginValidator: LoginRequestValidator,
    private val logoutValidator: LogoutRequestValidator
) {

    suspend fun profile(req: ServerRequest): ServerResponse = ok().bodyValueAndAwait(keycloakService.profile())

    suspend fun timedRequest(req: ServerRequest): ServerResponse = ok().bodyValueAndAwait(authService.wellKnown())

    suspend fun adminPage(req: ServerRequest): ServerResponse = ok().bodyValueAndAwait(authService.wellKnown())

    suspend fun signUp(signUpRequest: ServerRequest): ServerResponse {
        val locale = retrieveLocale(signUpRequest)
        val body = retrieveRequiredBodyOrFail<SignUpRequest>(signUpRequest, locale)
        signValidator.validate(body,locale)
        return ok().bodyValueAndAwait(keycloakService.signUp(body, locale))
    }

    suspend fun login(loginRequest: ServerRequest): ServerResponse {
        val locale = retrieveLocale(loginRequest)
        val body = retrieveRequiredBodyOrFail<LoginRequest>(loginRequest, locale)
        loginValidator.validate(body, locale)
        return ok().bodyValueAndAwait(keycloakService.login(body, locale))
    }

    suspend fun logout(logoutRequest: ServerRequest): ServerResponse {
        val locale = retrieveLocale(logoutRequest)
        val body = retrieveRequiredBodyOrFail<LogoutRequest>(logoutRequest, locale)
        return ok().bodyValueAndAwait(keycloakService.logout(body))
    }

    suspend fun deleteUser(request: ServerRequest): ServerResponse {
        return ok().bodyValueAndAwait(
            keycloakService.deleteUser(request.pathVariable("user-id"), retrieveLocale(request))
        )
    }

    private suspend inline fun<reified B: Any> retrieveRequiredBodyOrFail(request: ServerRequest, locale: Locale): B {
        if (request.headers().contentLength().asLong <= 0) {
            throw CustomException(
                httpStatus = 400,
                errorMessage = messageResolver.getErrorMessage(KEY_REQUEST_BODY, locale)
            )
        }
        return request.awaitBody<B>()
    }
}