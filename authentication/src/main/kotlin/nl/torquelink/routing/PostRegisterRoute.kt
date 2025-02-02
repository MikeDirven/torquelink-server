package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.data.anyOf
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.services.AuthenticationService
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.auth.RegistrationRequests
import kotlin.reflect.typeOf

fun postRegisterRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "Register to Torque Link."
    request {
        body(
            anyOf(
                typeOf<RegistrationRequests.RegisterWithTorqueLinkDto>()
            )
        )
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

fun Routing.postRegisterRoute() {
    post("auth/register", ::postRegisterRouteDoc) {
        val request = call.receive<RegistrationRequests>()
        AuthenticationService().registerNewIdentity(request)

        call.respond(HttpStatusCode.OK)
    }
}