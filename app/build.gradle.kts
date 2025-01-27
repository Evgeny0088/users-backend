
plugins {
    id("library-conventions")
    id("spring-conventions")
}

version=project.properties["app-version"]!!
group=project.properties["app-group"]!!

dependencies {
    implementation(project(":modules:auth-module"))
    implementation(project(":modules:users-module"))
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}