package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*
import kotlinx.serialization.Contextual
import nl.torquelink.shared.filters.Filters
import nl.torquelink.shared.routing.TorqueLinkRouting

interface TorqueLinkUserRoutingV1 {
    @Resource("users")
    class Users(
        val parent: TorqueLinkRouting.Api.V1 = TorqueLinkRouting.Api.V1(),
        val page: Int? = null,
        val limit: Int? = null,
        @Contextual val filters: Filters? = null
    )
}