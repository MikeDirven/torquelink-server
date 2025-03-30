package nl.torquelink.routing.groups.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.get
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.groups.GroupDao
import nl.torquelink.database.pagination.paginated
import nl.torquelink.database.tables.groups.GroupTable
import nl.torquelink.routing.groups.constants.GroupsRoutingConstants
import nl.torquelink.shared.filters.exposed.createSqlExpression
import nl.torquelink.shared.filters.ktor.addFilters
import nl.torquelink.shared.filters.ktor.filters
import nl.torquelink.shared.models.Pageable
import nl.torquelink.shared.models.group.Groups
import nl.torquelink.shared.pagination.ktor.addPagination
import nl.torquelink.shared.routing.subRouting.TorqueLinkGroupRoutingV1

fun getGroupsRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(GroupsRoutingConstants.TAG)
    description = "Get groups"
    securitySchemeNames(SECURITY_SCHEME)
    addFilters()
    addPagination()
    response {
        HttpStatusCode.OK to {
            body<Pageable<Groups.GroupDto>>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.getGroupsRoute() {
    get<TorqueLinkGroupRoutingV1.Groups>(::getGroupsRouteDoc) {
        TorqueLinkDatabase.executeAsync {
            val filters = filters()
            val loadedGroups: Pageable<Groups.GroupDto> = GroupDao.paginated(
                converter = GroupDao::toGroupDto,
                filter = GroupTable.createSqlExpression(filters)
            )

            call.respond(HttpStatusCode.OK, loadedGroups)
        }
    }
}