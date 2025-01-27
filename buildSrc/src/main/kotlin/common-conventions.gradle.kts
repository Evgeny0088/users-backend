import gradle.kotlin.dsl.accessors._d9dcfd1a467b0b6fe90c5571a57aa558.implementation
import gradle.kotlin.dsl.accessors._d9dcfd1a467b0b6fe90c5571a57aa558.testImplementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava{
    options.encoding = "UTF-8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}
