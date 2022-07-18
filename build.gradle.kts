import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    base
    kotlin("jvm") version "1.7.10" apply false
}

allprojects {
    group = "rs.emulate"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}

subprojects {
    afterEvaluate {
        plugins.withType<KotlinPluginWrapper> {
            dependencies {
                val testImplementation by configurations
                testImplementation(kotlin("test"))
                testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
                testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
                testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
                testImplementation("io.mockk:mockk:1.7.15")

                val testRuntimeOnly by configurations
                testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
            }
        }
    }
}