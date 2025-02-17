import gradle.kotlin.dsl.accessors._d9dcfd1a467b0b6fe90c5571a57aa558.implementation

plugins {
    java
    `java-library`
}

dependencies {
    implementation("org.liquibase:liquibase-core:${properties["liquibaseCoreVersion"]}")
}
