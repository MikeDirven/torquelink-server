package nl.torquelink.nl.torquelink.routing.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.nl.torquelink.routing.users.routes.*

fun Application.configureUsersRouting() {
    routing {
        authenticate(SECURITY_SCHEME) {
            getUserProfilesRoute()
            getUserProfileRoute()
            postUserProfileRoute()
            patchUserProfileRoute()

            postUserCarRoute()
        }
    }
}