
plugins {
    id("library-conventions")
    id("spring-conventions")
    id("email-conventions")
}

dependencies {
    implementation(project(":modules:service-config-module"))
}