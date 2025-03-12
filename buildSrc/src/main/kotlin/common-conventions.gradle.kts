
plugins {
    id("org.jetbrains.kotlin.jvm")
    id("detekt-conventions")
    id("jacoco-conventions")
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["kotlinCoroutinesVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${properties["kotlinVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${properties["kotlinCoroutinesVersion"]}")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${properties["kotlinReactorExtensionVersion"]}")
    implementation("org.apache.commons:commons-lang3:${properties["apacheCommonsVersion"]}")
    implementation("commons-validator:commons-validator:${properties["apacheValidatorVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter:${properties["junitVersion"]}")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:${properties["kotlinTestVersion"]}")
    testImplementation("io.projectreactor:reactor-test:${properties["projectReactorTestVersion"]}")
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava{
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
    finalizedBy(tasks.jacocoTestReport)
}
