package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.get
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.services.AuthenticationService
import nl.torquelink.shared.routing.subRouting.TorqueLinkAuthRouting

fun getVerifyEmailRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "Verify email with verification token."
    request {
        queryParameter<String>("verification")
    }
    response {
        HttpStatusCode.OK to {}
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.getVerifyEmailRoute() {
    get<TorqueLinkAuthRouting.Email.Verify>(::getVerifyEmailRouteDoc) { resource ->
        AuthenticationService().verifyEmail(
            resource.verification
        )

        call.respond(HttpStatusCode.OK)
    }
}