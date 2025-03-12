object Constants {

    val excludedFiles = listOf(
        "**/Constants.class",
        "**/*Starter.class",
        "**/config/*.class",
        "**/dto/*.class",
        "**/properties/*.class",
        "**/entity/*.class",
        "**/enums/*.class",
        "**/enum/*.class"
    )

    const val jacocoExecPatternName = "**/build/jacoco/*.exec"
    const val jacocoMergedReportsPath = "reports/jacoco/mergedCoverageReport"
}