package auth.module.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterController {

    @Bean
    fun routerUsers(authHandler: AuthHandler): RouterFunction<ServerResponse> {
        return coRouter {
            "/api/v1/auth".nest {
                /*  for test only*/ GET("/profile", authHandler::profile)
                POST("/sign-up", authHandler::signUp)
                POST("/login", authHandler::login)
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
            })
    }
}