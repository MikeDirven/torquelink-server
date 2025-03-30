package nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.patch
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.domain.environment.Environment
import nl.torquelink.extensions.identity
import nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.routing.users.exception.UserApiExceptions
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1
import java.io.File

fun patchUserProfileAvatarRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "update user profile avatar"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        pathParameter<Long>("userId")
        multipartBody {
            mediaTypes(ContentType.MultiPart.FormData)
            part<File>("avatar"){
                mediaTypes = setOf(
                    ContentType.Image.PNG,
                    ContentType.Image.JPEG,
                    ContentType.Image.GIF
                )
            }
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

fun Route.patchUserProfileAvatarRoute() {
    patch<TorqueLinkUserRoutingV1.Profiles.ByUserId.Avatar>(::patchUserProfileAvatarRouteDoc) { resource ->
        val identity = identity()
        val request = call.receiveMultipart().readPart()?.takeIf { it is PartData.FileItem } as? PartData.FileItem
            ?: throw UserApiExceptions.NoProfileAvatarInRequest

        val response =  TorqueLinkDatabase.executeAsync {
            val profile = UserProfileDao.find {
                UserProfileTable.identity eq identity.id
            }.singleOrNull() ?: throw UserApiExceptions.UserProfileNotFoundException

            val avatarFile = File(
                Environment.profileAvatarStorage, "profile_avatar_${resource.parent.userId}.png"
            ).also{ if(!it.exists()) it.createNewFile() }

            request.provider().copyTo(avatarFile.writeChannel())

            profile.apply {
                avatar = avatarFile.absolutePath
                avatarUrl = href(
                    ResourcesFormat(),
                    TorqueLinkUserRoutingV1.Profiles.ByUserId.Avatar(
                        parent = TorqueLinkUserRoutingV1.Profiles.ByUserId(
                            userId = profile.id.value,
                        )
                    )
                )
            }
        }

        call.respond(HttpStatusCode.OK, response.toResponseWithoutSettings())
    }
}