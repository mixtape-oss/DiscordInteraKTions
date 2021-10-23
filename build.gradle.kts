import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    `maven-publish`
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.perfectdreams.net")
    }

    group = "net.perfectdreams.discordinteraktions"
    version = "0.0.17-SNAPSHOT"

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    configure<PublishingExtension> {
        publications {
            register<MavenPublication>("dimensional") {
                from(components["java"])

                group = project.group as String
                version = project.version as String
                artifactId = project.name

                artifact(sourcesJar)
            }
        }

        repositories {
            maven("https://dimensional.jfrog.io/artifactory/maven") {
                name = "dimensional"
                credentials {
                    username = System.getProperty("USERNAME") ?: System.getenv("USERNAME")
                    password = System.getProperty("PASSWORD") ?: System.getenv("PASSWORD")
                }
            }
        }
    }
}
