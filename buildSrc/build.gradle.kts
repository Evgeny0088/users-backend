
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${properties["kotlinVersion"]}")
    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:${properties["kotlinVersion"]}")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:${properties["springDependencyManagementVersion"]}")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${properties["springBootVersion"]}")
    implementation("org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:${properties["kotlinVersion"]}")
    implementation("org.liquibase.gradle:org.liquibase.gradle.gradle.plugin:${properties["liquibasePluginVersion"]}")
    implementation("org.jetbrains.kotlin.plugin.jpa:org.jetbrains.kotlin.plugin.jpa.gradle.plugin:${properties["kotlinJpaPluginVersion"]}")

}
