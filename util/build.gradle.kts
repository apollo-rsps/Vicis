plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray")
}

description = "Vicis Utilities"
ext["moduleName"] = "rs.emulate.util"

val bouncycastleVersion: String by project
val commonsCompressVersion : String by project
val guavaVersion : String by project
val nettyVersion: String by project

dependencies {
    api("io.netty", "netty-buffer", nettyVersion)
    implementation("org.bouncycastle", "bcprov-jdk15on", bouncycastleVersion)
    implementation("org.apache.commons", "commons-compress", commonsCompressVersion)
    implementation("com.google.guava", "guava", guavaVersion)
    implementation("org.tukaani:xz:1.8")
    implementation(kotlin("stdlib"))
}
