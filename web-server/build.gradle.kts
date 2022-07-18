plugins {
    kotlin("jvm")
    java
    application
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
    implementation("dev.forst:ktor-openapi-generator:0.4.4")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")
    implementation("io.ktor:ktor-serialization-jackson:2.0.3")
    implementation("io.ktor:ktor-server-default-headers:2.0.3")
    implementation("io.ktor:ktor-server-status-pages:2.0.3")
    implementation("io.ktor:ktor-server-cors:2.0.3")
    implementation("io.ktor:ktor-server-conditional-headers:2.0.3")
    implementation("io.ktor:ktor-server-partial-content:2.0.3")
    implementation("io.ktor:ktor-server-auto-head-response:2.0.3")
    implementation("io.ktor:ktor-server-call-logging:2.0.3")
    implementation("io.ktor:ktor-server-compression:2.0.3")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.3")
    implementation("io.ktor:ktor-server-html-builder:2.0.3")
    implementation("io.ktor:ktor-serialization-gson:2.0.3")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.ktor:ktor-server-netty-jvm:2.0.3")
    implementation("io.ktor:ktor-server-sessions-jvm:2.0.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.0.3")

    implementation(project(":legacy"))
    implementation(project(":modern"))
}