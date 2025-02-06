@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
}

group = "nl.torquelink"
version = "0.0.0.5"

fun MavenArtifactRepository.githubCredentials() {
    credentials {
        username = System.getenv("GITHUB_USER")
        password = System.getenv("GITHUB_KEY")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHub"
            url = uri("https://maven.pkg.github.com/MikeDirven/torquelink-server")
            githubCredentials()
        }
    }
}

kotlin {
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    wasmJs()
    jvmToolchain(17)
    jvm() {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
        withJava()
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