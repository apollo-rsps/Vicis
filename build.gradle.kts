import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.61" apply false
    id("org.openjfx.javafxplugin") version "0.0.8" apply false
    id("com.github.ben-manes.versions") version "0.27.0"
    id("com.jfrog.bintray") version "1.8.4" apply (false)
}

allprojects {
    group = "rs.emulate"

    repositories {
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/michaelbull/maven")
        jcenter()
    }
}

subprojects {
    afterEvaluate {
        plugins.withType<KotlinPluginWrapper> {
            dependencies {
                val api by configurations
                api("com.michael-bull.kotlin-result:kotlin-result:1.1.4")

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

        plugins.withType<BintrayPlugin> {
            configure<BintrayExtension> {
                user = System.getenv("BINTRAY_USER")
                key = System.getenv("BINTRAY_KEY")
                publish = true
                setPublications("maven")

                pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
                    name = rootProject.name
                    userOrg = "apollo-rsps"
                    desc = description
                    repo = property("bintray.repository") as String

                    setLicenses("ISC")
                })
            }
        }

        plugins.withType<MavenPublishPlugin> {
            configure<PublishingExtension> {
                publications.create<MavenPublication>("maven") {
                    from(components["java"])

                    groupId = project.group as String
                    artifactId = project.name
                    version = project.version as String
                }
            }
        }

        plugins.withType<JavaPlugin> {
            configure<JavaPluginExtension> {
                withSourcesJar()

                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}
