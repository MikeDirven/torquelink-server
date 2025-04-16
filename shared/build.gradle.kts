@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
}

group = "nl.torquelink"
version = "0.0.0.13-alpha1"

fun MavenArtifactRepository.githubCredentials() {
    credentials {
        username = System.getenv("GITHUB_USER")
        password = System.getenv("GITHUB_KEY")
    }
}

publishing {
    repositories {
        mavenLocal()
        maven {
            name = "GitHub"
            url = uri("https://maven.pkg.github.com/MikeDirven/torquelink-server")
            githubCredentials()
        }
    }
}

kotlin {
    jvmToolchain(17)
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    wasmJs {
        browser {
            binaries.library()
        }
    }
    jvm() {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.common.resources)
//                implementation(libs.bundles.ktor.client)
                implementation(libs.jetbrains.kotlinx.serialization )
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.bundles.ktor)
                implementation(libs.bundles.exposed)
                implementation(libs.bundles.smiley)
                implementation(libs.cipher.bouncycastle)
            }
        }
    }
}