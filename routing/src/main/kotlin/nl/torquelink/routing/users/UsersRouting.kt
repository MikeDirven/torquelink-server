package nl.torquelink.nl.torquelink.routing.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.nl.torquelink.routing.users.routes.getUserProfilesRoute
import nl.torquelink.nl.torquelink.routing.users.routes.patchUserProfileRoute
import nl.torquelink.nl.torquelink.routing.users.routes.postUserProfileRoute

fun Application.configureUsersRouting() {
    routing {
        authenticate(SECURITY_SCHEME) {
            getUserProfilesRoute()
            postUserProfileRoute()
            patchUserProfileRoute()
        }
    }
}