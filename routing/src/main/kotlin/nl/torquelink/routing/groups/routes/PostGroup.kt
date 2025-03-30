package nl.torquelink.routing.groups.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.groups.GroupDao
import nl.torquelink.database.dao.groups.GroupMemberDao
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.extensions.identity
import nl.torquelink.routing.groups.constants.GroupsRoutingConstants
import nl.torquelink.routing.users.exception.UserApiExceptions
import nl.torquelink.shared.enums.group.GroupMemberRole
import nl.torquelink.shared.models.group.Groups
import nl.torquelink.shared.routing.subRouting.TorqueLinkGroupRoutingV1
import org.jetbrains.exposed.sql.SizedCollection

fun postGroupRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(GroupsRoutingConstants.TAG)
    description = "Create group"
    securitySchemeNames(SECURITY_SCHEME)
    request {
        body<Groups.GroupCreateDto>()  // replace with actual group id
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

fun Route.postGroupRoute() {
    post<TorqueLinkGroupRoutingV1.Groups>(::postGroupRouteDoc) { resource ->
        TorqueLinkDatabase.executeAsync {
            val request = call.receive<Groups.GroupCreateDto>()
            val response =  TorqueLinkDatabase.execute {
                val identity = identity()

                val currentProfile = UserProfileDao.find {
                    UserProfileTable.identity eq identity.id
                }.singleOrNull() ?: throw UserApiExceptions.UserProfileNotFoundException

                // Create the group entity
                val newGroup = GroupDao.new {
                    groupName = request.groupName
                    description = request.description
                    privateGroup = request.privateGroup
                    joinRequestsEnabled  = request.joinRequestsEnabled
                    memberListVisibility = request.memberListVisibility
                    facebookUrl = request.facebookUrl
                    twitterUrl = request.twitterUrl
                    instagramUrl = request.instagramUrl
                    linkedInUrl = request.linkedInUrl
                    websiteUrl = request.websiteUrl
                }

                // Create the group member entity for the user that creates the group
                val newGroupMember = GroupMemberDao.new{
                    group = newGroup
                    user = currentProfile
                    role = GroupMemberRole.ADMIN
                    notifications = false
                }

                // Add the group member to the group
                newGroup.members = SizedCollection(
                    newGroup.members.plus(newGroupMember)
                )

                // Return the group with details

                newGroup.toGroupWithDetailsDto()
            }

            call.respond(HttpStatusCode.OK, response)
        }
    }
}