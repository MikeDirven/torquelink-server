package nl.torquelink.nl.torquelink.routing.groups

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME

fun Application.configureGroupsRouting() {
    routing {
        authenticate(SECURITY_SCHEME){

        }
    }
}