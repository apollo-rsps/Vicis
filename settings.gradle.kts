rootProject.name = "vicis"

include("util", "legacy", "modern", "scene3d", "common", "cache", "intellij")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}
