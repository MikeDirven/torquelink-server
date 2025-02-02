plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "nl.torquelink"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.ktor.client)

    implementation(project(":domain"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}