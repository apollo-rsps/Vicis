plugins {
    kotlin("jvm")
    `java-library`
}

ext["moduleName"] = "rs.emulate.legacy"

val commonsCompressVersion: String by project
val guavaVersion: String by project
val nettyVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.apache.commons", "commons-compress", commonsCompressVersion)
    implementation("com.google.guava", "guava", guavaVersion)

    api(project(":common"))
    api(project(":util"))
}

