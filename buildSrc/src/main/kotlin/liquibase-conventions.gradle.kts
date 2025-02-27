
plugins {
    java
    `java-library`
}

dependencies {
    implementation("org.liquibase:liquibase-core:${properties["liquibaseCoreVersion"]}")
}
