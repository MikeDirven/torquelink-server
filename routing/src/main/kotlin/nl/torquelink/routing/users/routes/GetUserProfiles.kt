package nl.torquelink.nl.torquelink.routing.users.routes

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.resources.get
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.SECURITY_SCHEME
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.pagination.paginated
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.nl.torquelink.routing.users.constants.UsersRoutingConstants
import nl.torquelink.shared.filters.exposed.createSqlExpression
import nl.torquelink.shared.filters.ktor.addFilters
import nl.torquelink.shared.filters.ktor.filters
import nl.torquelink.shared.models.Pageable
import nl.torquelink.shared.models.profile.UserProfiles
import nl.torquelink.shared.pagination.ktor.addPagination
import nl.torquelink.shared.routing.subRouting.TorqueLinkUserRoutingV1

fun getUserProfilesRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(UsersRoutingConstants.TAG)
    description = "Get user profiles"
    securitySchemeNames(SECURITY_SCHEME)
    addFilters()
    addPagination()
    response {
        HttpStatusCode.OK to {
            body<Any>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Route.getUserProfilesRoute() {
    get<TorqueLinkUserRoutingV1.Profiles>(::getUserProfilesRouteDoc) {
        TorqueLinkDatabase.executeAsync {
            val filters = filters()
            val loadedUserProfiles: Pageable<UserProfiles.UserProfileDto> = UserProfileDao.paginated(
                converter = UserProfileDao::toResponseWithoutSettings,
                filter = UserProfileTable.createSqlExpression(filters)
            )

            call.respond(HttpStatusCode.OK, loadedUserProfiles)
        }
    }
}