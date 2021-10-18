plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.kord:kord-common:0.8.x-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}
