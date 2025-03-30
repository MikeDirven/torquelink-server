package nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.extensions.identity
import nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1
import java.time.LocalDate

fun postUserProfilesRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "create user profiles"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        body<UserProfiles.UserProfileCreateDto>()
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
        val request = call.receive<UserProfiles.UserProfileCreateDto>()
        val response =  TorqueLinkDatabase.execute {
            UserProfileDao.new {
                identity = identity()
                firstName = request.firstName
                lastName = request.lastName
                dateOfBirth = LocalDate.parse(request.dateOfBirth)
                phoneNumber = request.phoneNumber
                country = request.country
                city = request.city

                emailIsPublic = request.emailIsPublic
                firstNameIsPublic = request.firstNameIsPublic
                lastNameIsPublic = request.lastNameIsPublic
                dateOfBirthIsPublic = request.dateOfBirthIsPublic
                phoneNumberIsPublic = request.phoneNumberIsPublic
                countryIsPublic = request.countryIsPublic
                cityIsPublic = request.cityIsPublic
            }.toResponseWithoutSettings()
        }

        call.respond(HttpStatusCode.OK, response)
    }
}