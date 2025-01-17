plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

repositories {
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    implementation("net.dv8tion:JDA:4.3.0_283")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
