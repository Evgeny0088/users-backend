package auth.module.controller

import auth.module.dto.LoginRequest
import auth.module.dto.TokenResponse
import auth.module.service.AuthService
import auth.module.service.AuthServiceMock
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import users.module.dto.EmployeeDto

@Configuration
class RouterController {

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/api/v1/auth/login",
            produces = [MediaType.APPLICATION_JSON_VALUE],
            method = [RequestMethod.POST],
            beanClass = AuthService::class,
            beanMethod = "login",
            operation = Operation(
                operationId = "login",
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        description = "login",
                        content = arrayOf(Content(schema = Schema(implementation = TokenResponse::class)))
                    )
                ],
                requestBody = RequestBody(
                    content = arrayOf(Content(schema = Schema(implementation = LoginRequest::class)))
                )
            )
        ),
        RouterOperation(
            path = "/api/v1/test/profile",
            produces = [MediaType.APPLICATION_JSON_VALUE],
            method = [RequestMethod.POST],
            beanClass = AuthServiceMock::class,
            beanMethod = "testProfile",
            operation = Operation(
                operationId = "testProfile",
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        description = "test-profile",
                        content = arrayOf(Content(schema = Schema(implementation = String::class)))
                    )
                ],
                requestBody = RequestBody(
                    content = arrayOf(Content(schema = Schema(implementation = EmployeeDto::class)))
                )
            )
        )
    )
    fun routerUsers(authHandler: AuthHandler): RouterFunction<ServerResponse> {
        return coRouter {
            "/api/v1/auth".nest {
                /*  for test only*/ GET("/profile", authHandler::profile)
                POST("/sign-up", authHandler::signUp)
                POST("/login", authHandler::login)
                POST("/refresh", authHandler::refresh)
                POST("/logout", authHandler::logout)
                DELETE("/{user-id}", authHandler::deleteUser)
            }
        }
            .and(coRouter {
                "/api/v1/employees".nest {
                    POST("/save", authHandler::testSave)
                    GET("/department/{dep-name}", authHandler::getDepartment)
                }
            })
            .and(coRouter {
                GET("/api/v1/admin", authHandler::adminPage)
                GET("/api/v1/user", authHandler::adminPage)
            })
            .and(coRouter {
                POST("/api/v1/test/profile", authHandler::swaggerProfile)
            })
    }
}
