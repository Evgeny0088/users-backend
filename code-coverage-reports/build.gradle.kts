
plugins {
    java
    id("jacoco-conventions")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val projectsWithCoverage = listOf(":modules", ":app").map(::project).flatMap { it.subprojects }

val testCoverageReport by tasks.creating(JacocoReport::class) {
    description = "Collecting all jacoco reports in one file."
    group = "coverage"
    projectsWithCoverage.forEach {
        executionData(fileTree(it.projectDir.absolutePath).include(Constants.jacocoExecPatternName))
        it.afterEvaluate {
            val main = it.extensions.findByType(SourceSetContainer::class)?.get("main")
            sourceSets(main)
            classDirectories.setFrom(files(classDirectories.files.map {file->
                fileTree(file) {
                    setExcludes(Constants.excludedFiles)
                }
            }))
        }
    }

    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("${project.layout.buildDirectory.get()}/${Constants.jacocoMergedReportsPath}/xml/mergedCoverageReport.xml"))
        html.outputLocation.set(file("${project.layout.buildDirectory.get()}/${Constants.jacocoMergedReportsPath}/html"))
        csv.required.set(false)
    }
}