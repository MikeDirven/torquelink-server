package nl.torquelink.nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.patch
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.extensions.identity
import nl.torquelink.nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.nl.torquelink.routing.users.exception.UserApiExceptions
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1
import org.jetbrains.exposed.sql.Expression
import java.time.LocalDate

fun patchUserProfilesRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "update user profile"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        pathParameter<Long>("userId")
        body<UserProfiles.UserProfileUpdateDto>()
    }
    response {
        HttpStatusCode.OK to {
            body<UserProfiles.UserProfileWithSettingsDto>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.patchUserProfileRoute() {
    patch<TorqueLinkUserRoutingV1.Profiles>(::patchUserProfilesRouteDoc) {
        val request = call.receive<UserProfiles.UserProfileUpdateDto>()
        val response =  TorqueLinkDatabase.execute {
            val identity = this.identity()

            UserProfileDao.findSingleByAndUpdate(
                Expression.build {
                    UserProfileTable.identity eq identity.id
                }
            ) {
                it.firstName = request.firstName ?: it.firstName
                it.lastName = request.lastName ?: it.lastName
                it.dateOfBirth = request.dateOfBirth?.let { LocalDate.parse(it) } ?: it.dateOfBirth
                it.phoneNumber = request.phoneNumber ?: it.phoneNumber
                it.country = request.country ?: it.country
                it.city = request.city ?: it.city

                it.emailIsPublic = request.emailIsPublic ?: it.emailIsPublic
                it.firstNameIsPublic = request.firstNameIsPublic ?: it.firstNameIsPublic
                it.lastNameIsPublic = request.lastNameIsPublic?: it.lastNameIsPublic
                it.dateOfBirthIsPublic = request.dateOfBirthIsPublic?: it.dateOfBirthIsPublic
                it.phoneNumberIsPublic = request.phoneNumberIsPublic?: it.phoneNumberIsPublic
                it.countryIsPublic = request.countryIsPublic?: it.countryIsPublic
                it.cityIsPublic = request.cityIsPublic?: it.cityIsPublic
            }?.toResponseWithSettings()
                ?: throw UserApiExceptions.UserProfileNotFoundException
        }

        call.respond(HttpStatusCode.OK, response)
    }
}