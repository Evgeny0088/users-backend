
plugins {
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${properties["detektVersion"]}")
}

detekt {
    source.setFrom("src/main/kotlin")
    config.setFrom("${rootProject.projectDir}/detekt/config.yaml")
    autoCorrect = true
    parallel = true
    debug = true
}




