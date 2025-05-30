plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "nl.torquelink"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}