
plugins {
    id("common-conventions")
}

dependencies {
    // logs
    implementation("org.slf4j:slf4j-api:${properties["s4jVersion"]}")
    implementation("ch.qos.logback:logback-classic:${properties["logbackVersion"]}")
    implementation("ch.qos.logback:logback-core:${properties["logbackVersion"]}")

    // tracing
    implementation(platform("io.micrometer:micrometer-tracing-bom:${properties["micrometerVersion"]}"))
    implementation("io.micrometer:micrometer-tracing")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // jackson mapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${properties["jacksonMapperVersion"]}")
}