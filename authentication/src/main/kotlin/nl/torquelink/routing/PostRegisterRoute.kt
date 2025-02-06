package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.services.AuthenticationService
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.auth.RegistrationRequests
import nl.torquelink.shared.routing.subRouting.TorqueLinkAuthRouting

fun postRegisterRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "Register to Torque Link."
    request {
        body<RegistrationRequests>()
    }
    response {
        HttpStatusCode.OK to {
            body<AuthenticationResponses>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.postRegisterRoute() {
    post<TorqueLinkAuthRouting.Register>(::postRegisterRouteDoc) {
        val request = call.receive<RegistrationRequests>()
        AuthenticationService().registerNewIdentity(request)

        call.respond(HttpStatusCode.OK)
    }
}