plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.kord:kord-rest:0.8.x-SNAPSHOT")
    implementation("io.ktor:ktor-server-netty:1.6.0")

    api(project(":requests-verifier"))
    api(project(":common"))
    api(project(":platforms:common-kord"))
    // Async Appender is broken in alpha5
    // https://stackoverflow.com/questions/58742485/logback-error-no-attached-appenders-found
    api("ch.qos.logback:logback-classic:1.3.0-alpha4")
}

tasks.test {
    useJUnitPlatform()
}
