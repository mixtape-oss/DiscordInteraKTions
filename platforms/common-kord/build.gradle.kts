plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.kord:kord-rest:0.8.x-SNAPSHOT")

    api(project(":common"))
}
