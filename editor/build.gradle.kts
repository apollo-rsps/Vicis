import org.gradle.internal.jvm.Jvm
import org.jetbrains.kotlin.contracts.model.structure.UNKNOWN_COMPUTATION.type
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    `java-library`
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    id("kotlinx-serialization") version "1.3.11"
    id("io.freefair.jsass-java") version "2.9.5"
}

ext["moduleName"] = "rs.emulate.util"

val classpathScannerVersion: String by project
val controlsFxVersion: String by project
val guiceVersion: String by project
val jomlVersion: String by project
val nettyVersion: String by project
val openjfxVersion: String by project
val rxkotlinFxVersion: String by project
val rxkotlinVersion: String by project
val tornadoFxVersion: String by project

fun DependencyHandler.kotlinx(module: String, version: String? = null): Any =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"

repositories {
    maven("https://dl.bintray.com/clearcontrol/ClearControl")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(kotlinx("coroutines-core", "1.0.1"))
    implementation(kotlinx("coroutines-javafx", "1.0.1"))
    implementation(kotlinx("coroutines-jdk8", "1.0.1"))

    implementation("org.controlsfx", "controlsfx", controlsFxVersion)
    implementation("org.openjfx", "javafx-controls", openjfxVersion)
    implementation("org.openjfx", "javafx-fxml", openjfxVersion)
    implementation("org.openjfx", "javafx-web", openjfxVersion)
    implementation("org.joml", "joml", jomlVersion)

    implementation("io.github.classgraph", "classgraph", "4.6.6")
    implementation("com.google.inject", "guice", guiceVersion)
    implementation("com.google.inject.extensions", "guice-assistedinject", guiceVersion)
    implementation("com.google.inject.extensions", "guice-multibindings", guiceVersion)

    implementation("net.clearcontrol:dockfx:0.1.12")
    implementation("com.dooapp.fxform2", "core", "9.0.0")
    implementation("com.panemu", "tiwulfx", "3.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.4")
    implementation("io.reactivex.rxjava2:rxjavafx:2.2.2")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("com.googlecode.cqengine:cqengine:3.0.0")

    // Default icon pack used for elements in the UI.
    implementation(group = "org.kordamp.ikonli", name = "ikonli-core", version = "11.0.2")
    implementation(group = "org.kordamp.ikonli", name = "ikonli-javafx", version = "11.0.2")
    implementation("org.kordamp.ikonli:ikonli-foundation-pack:11.0.2")
    implementation("org.hibernate.validator", "hibernate-validator", "6.0.2.Final")
    implementation("org.glassfish", "javax.el", "3.0.1-b09")

    implementation(project(":legacy"))
    implementation(project(":modern"))
    implementation(project(":scene3d"))
    implementation(project(":util"))
}

application {
    mainClassName = "rs.emulate.editor/rs.emulate.editor.WorkbenchApplication"
    applicationDefaultJvmArgs = listOf(
        "--illegal-access=warn",
        "--add-opens", "javafx.controls/javafx.scene.control.skin=ALL-UNNAMED",
        "--add-opens", "javafx.base/com.sun.javafx.runtime=controlsfx",
        "--add-opens", "javafx.graphics/javafx.scene=controlsfx",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.javafx.scene.input=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED",
        "--add-opens", "javafx.base/com.sun.javafx.runtime=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang=com.google.guice",
        "-Djdk.attach.allowAttachSelf=true"
    )
}

javafx {
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

task<Exec>("jlink") {
    workingDir = buildDir

    val jvm = Jvm.current()
    val jvmHome = jvm.javaHome
    val jvmJmodsHome = jvmHome.toPath().resolve("jmods")
    val jlinkClasspath = sourceSets["main"].runtimeClasspath +
        configurations.runtimeClasspath +
        fileTree(jvmJmodsHome) {
            include("*.jmod")
        }

    dependsOn(tasks.withType<KotlinCompile>())
    doFirst {
        commandLine = listOf(
            jvm.getExecutable("jlink").toString(),
            "--strip-debug",
            "--no-header-files",
            "--no-man-pages",
            "--module-path", jlinkClasspath.asPath,
            "--add-modules=rs.emulate.editor",
            "--output", "$buildDir/dist"
        )
    }
}
