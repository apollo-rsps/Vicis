plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    `maven-publish`
    id("com.jfrog.bintray")
}

description = "Vicis 3D SceneGraph"
ext["moduleName"] = "rs.emulate.scene3d"

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
        runtimeOnly("org.lwjgl", it, lwjglVersion, classifier = "natives-windows")
        runtimeOnly("org.lwjgl", it, lwjglVersion, classifier = "natives-linux")
    }
}

javafx {
    modules = listOf("javafx.controls")
}
