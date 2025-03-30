package nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserCarDao
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.extensions.identity
import nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.routing.users.exception.UserApiExceptions
import nl.torquelink.shared.models.profile.UserCars
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1

fun postUserCarRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "create user car"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        body<UserCars.UserCarCreateDto>()
    }
    response {
        HttpStatusCode.OK to {
            body<UserCars.UserCarWithEngineDetailsDto>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.postUserCarRoute() {
    post<TorqueLinkUserRoutingV1.Profiles.Cars>(::postUserCarRouteDoc) {
        val request = call.receive<UserCars.UserCarCreateDto>()
        val response =  TorqueLinkDatabase.execute {
            val identity = identity()

            val currentProfile = UserProfileDao.find {
                UserProfileTable.identity eq identity.id
            }.singleOrNull() ?: throw UserApiExceptions.UserProfileNotFoundException

            UserCarDao.new {
                user = currentProfile.id
                licensePlate = request.licensePlate
                vehicleType = request.vehicleType
                brand = request.brand
                tradeName = request.tradeName
                primaryColor = request.primaryColor
                secondaryColor = request.secondaryColor
                driveReadyMassWeight = request.driveReadyMassWeight
                variant = request.variant
                model = request.model

                // Engine details
                numberOfCylinders = request.numberOfCylinders
                cylinderCapacity = request.cylinderCapacity
                readyMassPower = request.readyMassPower
            }.toResponseWithEngineDetails()
        }

        call.respond(HttpStatusCode.OK, response)
    }
}