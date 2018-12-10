import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val commonsCompressVersion: String by project
val guavaVersion: String by project
val nettyVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.apache.commons", "commons-compress", commonsCompressVersion)
    implementation("com.google.guava", "guava", guavaVersion)

    // @TODO - remove when transitive dependency resolution is fixed for the API configuration
    implementation("io.netty", "netty-buffer", nettyVersion)

    api(project(":util"))
}

