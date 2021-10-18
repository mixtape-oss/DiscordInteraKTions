plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
}

group = "net.perfectdreams.discordinteraktions"

kotlin {
    explicitApi()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    api(project(":interaction-declarations"))
    api("dev.kord:kord-rest:0.8.x-SNAPSHOT")
    api("io.github.microutils:kotlin-logging:2.0.11")
}

//
//publishing {
//    publications {
//        register("PerfectDreams", MavenPublication::class.java) {
//            from(components["java"])
//        }
//    }
//}
