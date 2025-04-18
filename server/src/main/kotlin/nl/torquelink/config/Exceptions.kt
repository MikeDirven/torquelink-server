package nl.torquelink.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptions() {
    install(StatusPages) {
        exception<Exception>{ call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: cause.localizedMessage)
        }
    }
}
