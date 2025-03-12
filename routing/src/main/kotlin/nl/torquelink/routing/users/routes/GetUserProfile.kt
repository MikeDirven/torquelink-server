package nl.torquelink.nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.get
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.nl.torquelink.routing.users.exception.UserApiExceptions
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1

fun getUserProfileRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "Get user profile"
    securitySchemeNames(SECURITY_SCHEME)
    response {
        HttpStatusCode.OK to {
            body<UserProfiles.UserProfileDto>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.getUserProfileRoute() {
    get<TorqueLinkUserRoutingV1.Profiles.ByUserId>(::getUserProfilesRouteDoc) { resource ->
        TorqueLinkDatabase.executeAsync {
            val loadedUserProfiles: UserProfiles.UserProfileDto = UserProfileDao.findById(
                resource.userId
            )?.toResponseWithoutSettings()
                ?: throw UserApiExceptions.UserProfileIdNotFoundException(resource.userId)

            call.respond(HttpStatusCode.OK, loadedUserProfiles)
        }
    }
}