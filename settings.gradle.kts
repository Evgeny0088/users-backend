import java.io.FileFilter

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "users-backend"

fun includeNestedModules(vararg modules: String) {
    modules.forEach { dir ->
        File(rootDir, dir)
                .listFiles(
                        FileFilter {
                            it.isDirectory && File(it, "build.gradle.kts").exists()
                        })
                ?.forEach { include("$dir:${it.name}") }
    }
}

include("app")
includeNestedModules("modules")
include("modules:exceptionHandler-module")
findProject(":modules:exceptionHandler-module")?.name = "exceptionHandler-module"
include("modules:service-config-module")
findProject(":modules:service-config-module")?.name = "service-config-module"
