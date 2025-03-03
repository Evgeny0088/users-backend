
plugins {
    java
    id("org.jetbrains.kotlin.plugin.spring")
    kotlin("kapt")
}

kapt {
    keepJavacAnnotationProcessors = true
    javacOptions {
        option("-Xmaxerrs", 500.toString())
    }
}