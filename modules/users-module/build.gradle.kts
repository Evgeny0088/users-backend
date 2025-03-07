
plugins {
    id("library-conventions")
    id("spring-conventions")
    id("liquibase-conventions")
    id("kapt-conventions")
}

group="users.module"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:postgresql:${properties["postgresVersion"]}")
    implementation("org.postgresql:r2dbc-postgresql:${properties["postgresR2DBCVersion"]}")
    implementation("com.github.f4b6a3:ulid-creator:${properties["ulidGeneratorVersion"]}")
    kapt("org.mapstruct:mapstruct-processor:${properties["mapstructVersion"]}")
    implementation("org.mapstruct:mapstruct:${properties["mapstructVersion"]}")

    implementation(project(":modules:service-config-module"))
    implementation(project(":modules:exception-handler-module"))

}