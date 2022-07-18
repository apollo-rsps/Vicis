
plugins {
    kotlin("jvm")
    `maven-publish`
}

ext["moduleName"] = "rs.emulate.common"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    api(project(":util"))
}
