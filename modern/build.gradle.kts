plugins {
    kotlin("jvm")
}


val bouncycastleVersion: String by project
val guavaVersion : String by project
val nettyVersion: String by project
val jimfsVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":util"))

    implementation("com.google.guava", "guava", guavaVersion)
    implementation("org.bouncycastle", "bcprov-jdk15on", bouncycastleVersion)

    // @TODO - remove when transitive dependency resolution is fixed for the API configuration
    implementation("io.netty", "netty-buffer", nettyVersion)

    testImplementation("com.google.jimfs", "jimfs", jimfsVersion)
}
