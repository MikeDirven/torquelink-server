package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.database.dao.identity.AccessTokenStoreDao
import nl.torquelink.database.tables.identity.AccessTokenStoreTable
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.services.AuthenticationService
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.routing.subRouting.TorqueLinkAuthRouting
import org.jetbrains.exposed.sql.and

fun postSetNotificationTokenRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "Set notification token for identity"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        body<String>()
    }
    response {
        HttpStatusCode.OK to {}
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.postSetNotificationTokenRoute() {
    post<TorqueLinkAuthRouting.Notifications.Token>(::postSetNotificationTokenRouteDoc) {
        val authentication = call.principal<AuthenticationResponses>()
            ?: throw AuthExceptions.NoValidTokenFound
        val tokenFromRequest = call.receive<String>()

        // get currentIdentity
        val currentIdentity = AccessTokenStoreDao.find {
            AccessTokenStoreTable.accessToken eq authentication.accessToken and (
                    AccessTokenStoreTable.active eq true
                    )
        }.singleOrNull()?.identity ?: throw AuthExceptions.NoValidTokenFound

        AuthenticationService().setNotificationToken(currentIdentity.id.value, tokenFromRequest)

        call.respond(HttpStatusCode.OK)
    }
}