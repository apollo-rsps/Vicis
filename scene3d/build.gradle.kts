plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
}

description = "Vicis 3D SceneGraph"

val guavaVersion: String by project
val openjfxVersion: String by project
val jomlVersion : String by project
val lwjglVersion : String by project
val lwjglArtifacts = listOf("lwjgl", "lwjgl-glfw", "lwjgl-opengl")

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.guava", "guava", guavaVersion)

    api("org.joml", "joml", jomlVersion)

    lwjglArtifacts.forEach {
        implementation("org.lwjgl", it, lwjglVersion)
    }
}

javafx {
    modules = listOf("javafx.controls")
}
