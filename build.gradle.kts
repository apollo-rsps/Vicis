import groovy.xml.dom.DOMCategory.attributes
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    base
    kotlin("jvm") version "1.3.11" apply false
    id("org.openjfx.javafxplugin") version "0.0.5" apply false
    id("com.github.ben-manes.versions") version "0.20.0"
}

allprojects {
    group = "rs.emulate"
    version = "1.0"

    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    afterEvaluate {
        dependencies {
            add("testImplementation", kotlin("test"))
            add("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.3.1")
            add("testImplementation", "org.junit.jupiter:junit-jupiter-params:5.3.1")
            add("testImplementation", "org.junit.jupiter:junit-jupiter-api:5.3.1")
            add("testImplementation", "io.mockk:mockk:1.7.15")

            add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:5.3.1")
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        tasks.withType<JavaCompile>().configureEach {
            val kotlinCompileTask = tasks.findByName("compileKotlin") as KotlinCompile

            destinationDir = kotlinCompileTask.destinationDir
            dependsOn(kotlinCompileTask)

            doFirst {
                options.compilerArgs = listOf("--module-path", classpath.asPath)
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            println("Configuring $name in project ${project.name}...")

            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        tasks.withType<Jar> {
            inputs.property("moduleName", ext["moduleName"])

            manifest {
                attributes("Automatic-Module-Name" to ext["moduleName"])
            }
        }
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}
