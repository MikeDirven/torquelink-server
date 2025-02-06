package nl.torquelink.nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.identity.AccessTokenStoreDao
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.tables.identity.AccessTokenStoreTable
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1
import org.jetbrains.exposed.sql.and
import java.time.LocalDate

fun postUserProfilesRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "create user profiles"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        body<UserProfiles.UserProfileNewDto>()
    }
    response {
        HttpStatusCode.OK to {
            body<UserProfiles.UserProfileDto>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.postUserProfileRoute() {
    post<TorqueLinkUserRoutingV1.Profiles>(::postUserProfilesRouteDoc) {
        val request = call.receive<UserProfiles.UserProfileNewDto>()
        val response =  TorqueLinkDatabase.execute {
            val authentication = call.principal<AuthenticationResponses>()
                ?: throw AuthExceptions.NoValidTokenFound

            // get currentIdentity
            val currentIdentity = AccessTokenStoreDao.find {
                AccessTokenStoreTable.accessToken eq authentication.accessToken and (
                    AccessTokenStoreTable.active eq true
                )
            }.singleOrNull()?.identity ?: throw AuthExceptions.NoValidTokenFound

            UserProfileDao.new {
                identity = currentIdentity
                firstName = request.firstName
                lastName = request.lastName
                dateOfBirth = LocalDate.parse(request.dateOfBirth)
                phoneNumber = request.phoneNumber
                country = request.country
                city = request.city
            }.toResponseWithoutSettings()
        }

        call.respond(HttpStatusCode.OK, response)
    }
}