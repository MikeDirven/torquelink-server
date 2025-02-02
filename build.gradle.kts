plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ktor) apply false
//    alias(libs.plugins.kotlin.serialization) apply false
}

group = "nl.torquelink"
version = "0.0.1"

allprojects {
    apply(plugin = "maven-publish")
    repositories {
        mavenCentral()
        mavenLocal()
    }
}