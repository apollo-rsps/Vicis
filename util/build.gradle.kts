plugins {
    kotlin("jvm")
}

description = "Vicis Utilities"

val bouncycastleVersion: String by project
val commonsCompressVersion : String by project
val guavaVersion : String by project
val nettyVersion: String by project

dependencies {
    api("io.netty", "netty-buffer", nettyVersion)
    implementation("org.bouncycastle", "bcprov-jdk15on", bouncycastleVersion)
    implementation("org.apache.commons", "commons-compress", commonsCompressVersion)
    implementation("com.google.guava", "guava", guavaVersion)
    implementation(kotlin("stdlib"))
}
