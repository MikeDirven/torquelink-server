package nl.torquelink.config

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.*
import io.github.smiley4.ktorswaggerui.dsl.config.PluginConfigDsl
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.github.smiley4.schemakenerator.core.addDiscriminatorProperty
import io.github.smiley4.schemakenerator.core.connectSubTypes
import io.github.smiley4.schemakenerator.serialization.addJsonClassDiscriminatorProperty
import io.github.smiley4.schemakenerator.serialization.processKotlinxSerialization
import io.github.smiley4.schemakenerator.swagger.compileReferencingRoot
import io.github.smiley4.schemakenerator.swagger.data.TitleType
import io.github.smiley4.schemakenerator.swagger.generateSwaggerSchema
import io.github.smiley4.schemakenerator.swagger.withTitle
import io.ktor.server.application.*
import io.ktor.server.routing.*
import nl.torquelink.REFRESH_SECURITY_SCHEME
import nl.torquelink.SECURITY_SCHEME

fun Application.configureSwagger(){
    routing {
        route("torquelink-swagger.json") {
            openApiSpec()
        }
        route("swagger") {
            swaggerUI("/torquelink-swagger.json")
        }
    }
    install(SwaggerUI){
        info {
            title = "Torquelink API"
            version = "1.0.0"
            contact {
                name = "Torquelink Team"
                email = "info@torquelink.nl"
            }
        }
        swagger {
            displayOperationId = false
            showTagFilterInput = true
            sort = SwaggerUiSort.ALPHANUMERICALLY
            syntaxHighlight = SwaggerUiSyntaxHighlight.AGATE
            withCredentials = false
        }
        schemas {
            generator = { type ->
                type
                    .processKotlinxSerialization()
                    .connectSubTypes()
                    .addDiscriminatorProperty()
                    .addJsonClassDiscriminatorProperty()
                    .generateSwaggerSchema()
                    .withTitle(TitleType.SIMPLE)
                    .compileReferencingRoot()
            }
        }
        security {
            securityScheme(SECURITY_SCHEME) {
                type = AuthType.API_KEY
                scheme = AuthScheme.BEARER
                bearerFormat = "Bearer"
                location = AuthKeyLocation.HEADER
            }
            securityScheme(REFRESH_SECURITY_SCHEME) {
                type = AuthType.API_KEY
                scheme = AuthScheme.BEARER
                bearerFormat = "Bearer"
                location = AuthKeyLocation.HEADER
            }
        }

        specAssigner = { _, _ -> PluginConfigDsl.DEFAULT_SPEC_ID }
        pathFilter = { _, url -> url.firstOrNull() in listOf("api", "auth") }
        ignoredRouteSelectors = emptySet()
        outputFormat = OutputFormat.JSON
    }
}