package nl.torquelink.routing.groups

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.routing.groups.routes.getGroupRoute
import nl.torquelink.routing.groups.routes.getGroupsRoute
import nl.torquelink.routing.groups.routes.postGroupRoute

fun Application.configureGroupsRouting() {
    routing {
        authenticate(SECURITY_SCHEME){
            getGroupRoute()
            getGroupsRoute()
            postGroupRoute()
        }
    }
}