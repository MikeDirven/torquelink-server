package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.services.AuthenticationService
import nl.torquelink.shared.models.auth.ResetPasswordRequests
import nl.torquelink.shared.routing.subRouting.TorqueLinkAuthRouting

fun postResetPasswordRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "Request reset for password / reset password"
    request {
        body<ResetPasswordRequests>()
    }
    response {
        HttpStatusCode.OK to {}
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.postResetPasswordRoute() {
    post<TorqueLinkAuthRouting.Password.Reset>(::postResetPasswordRouteDoc) {
        val request = call.receive<ResetPasswordRequests>()

        when(request) {
            is ResetPasswordRequests.RequestPasswordReset -> {
                AuthenticationService().createPasswordResetRequest(
                    request.username,
                    request.email
                )
            }
            is ResetPasswordRequests.ResetPasswordWithTokenToken -> {
                AuthenticationService().resetPassword(
                    request.token,
                    request.newPassword
                )
            }
        }

        call.respond(HttpStatusCode.OK)
    }
}