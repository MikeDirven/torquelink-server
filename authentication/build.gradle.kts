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
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.smiley)
    implementation(libs.bundles.exposed)
    implementation(libs.apache.commons.email)

    implementation(project(":database"))
    implementation(project(":shared"))
}
