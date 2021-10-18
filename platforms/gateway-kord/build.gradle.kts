plugins {
    kotlin("jvm")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(kotlin("stdlib"))

    api(project(":common"))
    api(project(":platforms:common-kord"))

    implementation("dev.kord:kord-rest:0.8.x-SNAPSHOT")
    implementation("dev.kord:kord-gateway:0.8.x-SNAPSHOT")
}
