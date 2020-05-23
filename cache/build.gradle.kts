import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray")
}

ext["moduleName"] = "rs.emulate.cache"

val guavaVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.guava", "guava", guavaVersion)

    api(project(":common"))
    api(project(":legacy"))
    api(project(":modern"))
    api(project(":util"))
}
