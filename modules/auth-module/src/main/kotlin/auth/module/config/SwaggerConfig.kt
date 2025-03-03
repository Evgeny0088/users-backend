package auth.module.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.stereotype.Component

@OpenAPIDefinition(
    info = Info(
        contact = Contact(
            name = "Users",
            email = "contact@test.com",
            url = "http://any:8080"
        ),
        description = "OpenApi documentation for swagger",
        title = "OpenApi specification - users",
        version = "1.0",
        license = License(
            name = "Licence name",
            url = "https://some-url.com"
        ),
        termsOfService = "Terms of service"
    ),
    servers = [Server(
        description = "Local ENV",
        url = "http://localhost:8080"
    ), Server(
        description = "PROD ENV",
        url = "http://localhost:8080"
    )],
    security = [SecurityRequirement(
        name = "keycloak"
    )]
)
@SecurityScheme(
    name = "keycloak",
    description = "auth description",
    bearerFormat = "JWT",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP
)
@Component
class SwaggerConfig