package nl.torquelink.routing.groups.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.get
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.groups.GroupDao
import nl.torquelink.routing.groups.constants.GroupsRoutingConstants
import nl.torquelink.routing.groups.exceptions.GroupApiExceptions
import nl.torquelink.shared.models.group.Groups
import nl.torquelink.shared.routing.subRouting.TorqueLinkGroupRoutingV1

fun getGroupRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(GroupsRoutingConstants.TAG)
    description = "Get group"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        pathParameter<Long>("groupId")  // replace with actual group id
    }
    response {
        HttpStatusCode.OK to {
            body<Groups.GroupWithDetailsDto>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.getGroupRoute() {
    get<TorqueLinkGroupRoutingV1.Groups.ById>(::getGroupRouteDoc) { resource ->
        TorqueLinkDatabase.executeAsync {
            val loadedGroup: Groups.GroupWithDetailsDto = GroupDao.findById(
                resource.groupId
            )?.toGroupWithDetailsDto()
                ?: throw GroupApiExceptions.GroupIdNotFoundException(resource.groupId)

            call.respond(HttpStatusCode.OK, loadedGroup)
        }
    }
}