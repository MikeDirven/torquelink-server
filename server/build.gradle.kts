plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "nl.torquelink"
version = "0.0.1"

application {
    mainClass.set("nl.torquelink.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("torque-link-server")
        imageTag.set("0.0.4")
        portMappings.set(listOf(
            io.ktor.plugin.features.DockerPortMapping(
                80,
                8080,
                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
            )
        ))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.smiley)
    implementation(libs.bundles.exposed)

    implementation(project(":opendata"))

    implementation(project(":domain"))
    implementation(project(":shared"))
    implementation(project(":authentication"))
    implementation(project(":routing"))
}
