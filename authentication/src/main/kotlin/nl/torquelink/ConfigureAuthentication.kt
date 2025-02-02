package nl.torquelink

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.generators.TorqueLinkTokenGenerator
import nl.torquelink.routing.getRefreshToken
import nl.torquelink.routing.postLoginRoute
import nl.torquelink.routing.postRegisterRoute
import nl.torquelink.services.AuthenticationService

fun Application.configureAuthentication() {
    install(Authentication) {
        bearer(SECURITY_SCHEME) {
            authenticate { credential ->
                runBlocking {
                    AuthenticationService().checkTokenValidation(credential.token)
                }
            }
        }
        bearer(REFRESH_SECURITY_SCHEME) {
            authenticate { credential ->
                runBlocking {
                    AuthenticationService().checkTokenValidation(credential.token)
                }
            }
        }
    }

    // Initialize services
    AuthenticationService(
        application = this,
        database = TorqueLinkDatabase,
        tokenGenerator = TorqueLinkTokenGenerator
    )

    // Initialize routes
    routing {
        postLoginRoute()
        postRegisterRoute()

        getRefreshToken()
    }
}