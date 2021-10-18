plugins {
    kotlin("jvm")
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}
