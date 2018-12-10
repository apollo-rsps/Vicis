plugins {
    kotlin("jvm")
}

val nettyVersion: String by project
val jimfsVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":util"))

    // @TODO - remove when transitive dependency resolution is fixed for the API configuration
    implementation("io.netty", "netty-buffer", nettyVersion)

    testImplementation("com.google.jimfs", "jimfs", jimfsVersion)
}
