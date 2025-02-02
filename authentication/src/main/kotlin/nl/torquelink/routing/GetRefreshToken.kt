package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.REFRESH_SECURITY_SCHEME
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.shared.models.auth.AuthenticationResponses

fun getRefreshTokenRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "refresh token for torque link."
    securitySchemeNames(REFRESH_SECURITY_SCHEME)
    response {
        HttpStatusCode.OK to {
            body<AuthenticationResponses>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Routing.getRefreshToken() {
    authenticate(REFRESH_SECURITY_SCHEME){
        get("auth/refresh", ::getRefreshTokenRouteDoc) {
            val security = call.principal<AuthenticationResponses>()
                ?: throw AuthExceptions.NoValidTokenFound
            call.respond(HttpStatusCode.OK, security)
        }
    }
}