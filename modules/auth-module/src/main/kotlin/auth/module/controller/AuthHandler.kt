package auth.module.controller

import auth.module.dto.LoginRequest
import auth.module.dto.LogoutRequest
import auth.module.dto.RefreshRequest
import auth.module.dto.SignUpRequest
import auth.module.service.AuthServiceMock
import auth.module.service.AuthService
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
    private val authService: AuthService,
    private val authServiceMock: AuthServiceMock,
    private val messageResolver: ErrorsMessageResolver,
    private val validationHandler: ValidationHandler<Validator>,
    private val validators: List<Validator>
) {

    @PostConstruct
    fun init() {
        validationHandler.assignValidator(*validators.toTypedArray())
    }

    suspend fun profile(req: ServerRequest): ServerResponse =
        ok().bodyValueAndAwait(authService.profile())

    suspend fun testSave(req: ServerRequest): ServerResponse {
        val dto = retrieveRequiredBodyOrFail<EmployeeDto>(req)
        validationHandler.handleValidation(dto, req)
        return ok().bodyValueAndAwait(authService.saveTest(dto))
    }

    suspend fun swaggerProfile(req: ServerRequest): ServerResponse {
        val dto = retrieveRequiredBodyOrFail<EmployeeDto>(req)
        return ok().bodyValueAndAwait(authServiceMock.testProfile(dto))
    }

    suspend fun getDepartment(req: ServerRequest): ServerResponse {
        return ok().bodyValueAndAwait(
            authService.getDepartmentTest(req.pathVariable("dep-name"))
        )
    }

    suspend fun adminPage(req: ServerRequest): ServerResponse =
        ok().bodyValueAndAwait(authServiceMock.wellKnown())

    suspend fun signUp(signUpRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<SignUpRequest>(signUpRequest)
        validationHandler.handleValidation(body, signUpRequest)
        return ok().bodyValueAndAwait(authService.signUp(body))
    }

    suspend fun login(loginRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<LoginRequest>(loginRequest)
        validationHandler.handleValidation(body, loginRequest)
        return ok().bodyValueAndAwait(authService.login(body))
    }

    suspend fun refresh(refreshRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<RefreshRequest>(refreshRequest)
        validationHandler.handleValidation(body, refreshRequest)
        return ok().bodyValueAndAwait(authService.refreshToken(body))
    }

    suspend fun logout(logoutRequest: ServerRequest): ServerResponse {
        val body = retrieveRequiredBodyOrFail<LogoutRequest>(logoutRequest)
        validationHandler.handleValidation(body, logoutRequest)
        return ok().bodyValueAndAwait(authService.logout(body))
    }

    suspend fun deleteUser(request: ServerRequest): ServerResponse {
        return ok().bodyValueAndAwait(
            authService.deleteUser(request.pathVariable("user-id"))
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

