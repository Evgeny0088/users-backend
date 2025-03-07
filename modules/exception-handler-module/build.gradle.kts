plugins {
    id("library-conventions")
    id("spring-conventions")
}

dependencies {
    implementation(project(":modules:service-config-module"))
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:${properties["jakartaWsRsVersion"]}")
}