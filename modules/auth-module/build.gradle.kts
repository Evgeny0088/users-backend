
plugins {
    id("library-conventions")
    id("spring-conventions")
    id("swagger-conventions")
    id("kapt-conventions")
}

group="auth.module"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.keycloak:keycloak-admin-client:${properties["keycloakAdminVersion"]}")
    implementation("org.jboss.resteasy:resteasy-client:${properties["restEasyVersion"]}")

    implementation(project(":modules:exception-handler-module"))
    implementation(project(":modules:service-config-module"))
    implementation(project(":modules:instrumentation-module"))
    implementation(project(":modules:users-module"))
    implementation(project(":modules:email-module"))

    kapt("org.mapstruct:mapstruct-processor:${properties["mapstructVersion"]}")
    implementation("org.mapstruct:mapstruct:${properties["mapstructVersion"]}")
}