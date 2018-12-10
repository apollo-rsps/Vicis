plugins {
    application
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
}

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
    implementation(kotlinx("coroutines-core", "1.0.1"))
    implementation(kotlinx("coroutines-javafx", "1.0.1"))
    implementation(kotlinx("coroutines-jdk8", "1.0.1"))

    implementation("no.tornado", "tornadofx", tornadoFxVersion)
    implementation("org.controlsfx", "controlsfx", controlsFxVersion)
    implementation("com.github.thomasnield", "rxkotlinfx", rxkotlinFxVersion)
    implementation("io.reactivex.rxjava2", "rxkotlin", rxkotlinVersion)
    implementation("io.github.lukehutch", "fast-classpath-scanner", classpathScannerVersion)
    implementation("org.openjfx", "javafx-controls", openjfxVersion)
    implementation("org.openjfx", "javafx-fxml", openjfxVersion)
    implementation("org.openjfx", "javafx-web", openjfxVersion)
    implementation("org.joml", "joml", jomlVersion)

    implementation("io.github.classgraph", "classgraph", "4.6.6")
    implementation("com.google.inject", "guice", guiceVersion)
    implementation("com.google.inject.extensions", "guice-assistedinject", guiceVersion)
    implementation("com.google.inject.extensions", "guice-multibindings", guiceVersion)

    implementation("net.clearcontrol:dockfx:0.1.12")
    implementation("com.dlsc.formsfx", "formsfx-core", "1.3.0")
    implementation("com.panemu", "tiwulfx", "3.0")

    // @TODO - remove when transitive dependency resolution is fixed for the API configuration
    implementation("io.netty", "netty-buffer", nettyVersion)

    implementation(project(":legacy"))
    implementation(project(":scene3d"))
    implementation(project(":util"))
}

application {
    mainClassName = "rs.emulate.editor.core.workbench.WorkbenchApplication"
    applicationDefaultJvmArgs = listOf(
        "--illegal-access=warn",
        "--add-opens", "javafx.controls/javafx.scene.control.skin=ALL-UNNAMED",
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
