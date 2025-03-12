package nl.torquelink.nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.patch
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserCarDao
import nl.torquelink.database.dao.users.UserCarPhotoDao
import nl.torquelink.domain.environment.Environment
import nl.torquelink.nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.nl.torquelink.routing.users.exception.UserApiExceptions
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1
import java.io.File
import kotlin.random.Random

fun postUserCarPhotoRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "update user profile avatar"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        multipartBody {
            part<File>("avatar")
        }
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

fun Route.postUserCarPhotoRoute() {
    patch<TorqueLinkUserRoutingV1.Profiles.Cars.ByCarId.Photos>(::postUserCarPhotoRouteDoc) { resource ->
        val request = call.receiveMultipart().readPart()?.takeIf { it is PartData.FileItem } as? PartData.FileItem
            ?: throw UserApiExceptions.NoProfileAvatarInRequest

        val response =  TorqueLinkDatabase.executeAsync {
            val profileCar = UserCarDao.findById(resource.parent.carId)
                ?: throw UserApiExceptions.UserProfileIdNotFoundException(resource.parent.carId)

            val carPhotoFile = File(
                Environment.profileCarPhotoStorage, "Car_${resource.parent.carId}_${Random.nextLong()}.png"
            ).also{ if(!it.exists()) it.createNewFile() }

            request.provider().copyTo(carPhotoFile.writeChannel())

            profileCar.apply {
                UserCarPhotoDao.new {
                    userCar = profileCar.id
                    photoUrl = carPhotoFile.absolutePath
                }
            }
        }

        call.respond(HttpStatusCode.OK, response.toResponseWithoutEngineDetails())
    }
}