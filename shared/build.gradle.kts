import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "nl.torquelink"
version = "0.0.0.1"

kotlin {
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
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(project(":domain"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.cipher.bouncycastle)
            }
        }
    }
}