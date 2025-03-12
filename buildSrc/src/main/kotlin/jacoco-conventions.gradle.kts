
plugins {
    jacoco
    java
}

tasks.jacocoTestReport {
    classDirectories.setFrom(files(classDirectories.files.map { file->
        fileTree(file) {
            setExcludes(Constants.excludedFiles)
        }
    }))
    reports {
        xml.required = false
        csv.required = false
    }
    dependsOn(tasks.test)
}