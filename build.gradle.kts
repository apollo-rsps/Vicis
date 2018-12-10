import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
    base
    kotlin("jvm") version "1.3.11" apply false
    id("org.openjfx.javafxplugin") version "0.0.5" apply false
    id("com.github.ben-manes.versions" ) version "0.20.0"
}

allprojects {
    group = "rs.emulate"
    version = "1.0"

    repositories {
        mavenCentral()
        jcenter()
    }

    tasks.withType<KotlinCompile>().configureEach {
        println("Configuring $name in project ${project.name}...")

        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}
