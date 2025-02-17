
plugins {
    java
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("kapt")
}

kapt {
    keepJavacAnnotationProcessors = true
    javacOptions {
        option("-Xmaxerrs", 500.toString())
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${properties["springBootVersion"]}"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:${properties["javaxValidation"]}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}