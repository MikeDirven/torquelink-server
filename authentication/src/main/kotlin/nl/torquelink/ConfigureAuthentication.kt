package nl.torquelink

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.generators.TorqueLinkTokenGenerator
import nl.torquelink.providers.apiKey
import nl.torquelink.routing.getRefreshTokenRoute
import nl.torquelink.routing.getVerifyEmailRoute
import nl.torquelink.routing.postLoginRoute
import nl.torquelink.routing.postRegisterRoute
import nl.torquelink.services.AuthenticationService

fun Application.configureAuthentication() {
    install(Authentication) {
        apiKey(SECURITY_SCHEME) {
            headerName = SECURITY_SCHEME
            validate { credential ->
                runBlocking {
                    AuthenticationService().checkTokenValidation(credential)
                }
            }
        }
        apiKey(REFRESH_SECURITY_SCHEME) {
            headerName = REFRESH_SECURITY_SCHEME
            validate { credential ->
                runBlocking {
                    AuthenticationService().checkTokenValidation(credential)
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
        getVerifyEmailRoute()

        authenticate(REFRESH_SECURITY_SCHEME){
            getRefreshTokenRoute()
        }
    }
}