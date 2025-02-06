package nl.torquelink.nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.patch
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.identity.AccessTokenStoreDao
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.tables.identity.AccessTokenStoreTable
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.and
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
    patch<TorqueLinkUserRoutingV1.Profiles.ById>(::patchUserProfilesRouteDoc) { resource ->
        val request = call.receive<UserProfiles.UserProfileUpdateDto>()
        val response =  TorqueLinkDatabase.execute {
            val authentication = call.principal<AuthenticationResponses>()
                ?: throw AuthExceptions.NoValidTokenFound

            // get currentIdentity
            val currentIdentity = AccessTokenStoreDao.find {
                AccessTokenStoreTable.accessToken eq authentication.accessToken and (
                    AccessTokenStoreTable.active eq true
                )
            }.singleOrNull()?.identity ?: throw AuthExceptions.NoValidTokenFound

            UserProfileDao.findSingleByAndUpdate(
                Expression.build {
                    UserProfileTable.identity eq currentIdentity.id
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
            }?.toResponseWithSettings() ?: throw NotFoundException()
        }

        call.respond(HttpStatusCode.OK, response)
    }
}