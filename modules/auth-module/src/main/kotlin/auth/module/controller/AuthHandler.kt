package auth.module.controller

import auth.module.dto.LoginRequest
import auth.module.dto.LogoutRequest
import auth.module.dto.SignUpRequest
import auth.module.service.AuthService
import auth.module.service.KeycloakService
import auth.module.validation.SignUpValidator
import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.config.MessageKeys.KEY_REQUEST_BODY
import exception.handler.module.exception.CustomException
import exception.handler.module.validator.ValidationHandler
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import users.module.dto.EmployeeDto

@Service
class AuthHandler(
    private val keycloakService: KeycloakService,
    private val authService: AuthService,
    private val messageResolver: ErrorsMessageResolver,
    private val validationHandler: ValidationHandler<Validator>,
    private val signUpValidator: SignUpValidator
) {

    @PostConstruct
    fun init() {
        validationHandler.assignValidator(signUpValidator)
    }

    suspend fun profile(req: ServerRequest): ServerResponse =
        ok().bodyValueAndAwait(keycloakService.profile())

    suspend fun testSave(req: ServerRequest): ServerResponse {
        val dto = retrieveRequiredBodyOrFail<EmployeeDto>(req)
        validationHandler.handleValidation(dto, req)
        return ok().bodyValueAndAwait(keycloakService.saveTest(dto))
    }

    suspend fun getDepartment(req: ServerRequest): ServerResponse {
        return ok().bodyValueAndAwait(
            keycloakService.getDepartmentTest(req.pathVariable("dep-name"))
        )
    }

    suspend fun adminPage(req: ServerRequest): ServerResponse =
        ok().bodyValueAndAwait(authService.wellKnown())

    suspend fun signUp(signUpRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<SignUpRequest>(signUpRequest)
        validationHandler.handleValidation(body, signUpRequest)
        return ok().bodyValueAndAwait(keycloakService.signUp(body))
    }

    suspend fun login(loginRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<LoginRequest>(loginRequest)
        validationHandler.handleValidation(body, loginRequest)
        return ok().bodyValueAndAwait(keycloakService.login(body))
    }

    suspend fun logout(logoutRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<LogoutRequest>(logoutRequest)
        validationHandler.handleValidation(body, logoutRequest)
        return ok().bodyValueAndAwait(keycloakService.logout(body))
    }

    suspend fun deleteUser(request: ServerRequest): ServerResponse {
        return ok().bodyValueAndAwait(
            keycloakService.deleteUser(request.pathVariable("user-id"))
        )
    }

    private suspend inline fun<reified B: Any> retrieveRequiredBodyOrFail(request: ServerRequest): B {
        if (request.headers().contentLength().asLong <= 0) {
            throw CustomException(
                httpStatus = 400,
                errorMessage = messageResolver.getMessage(KEY_REQUEST_BODY)
            )
        }
        return request.awaitBody<B>()
    }
}

