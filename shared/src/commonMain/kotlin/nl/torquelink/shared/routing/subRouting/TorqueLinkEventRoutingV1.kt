package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*
import kotlinx.serialization.Contextual
import nl.torquelink.shared.filters.Filters
import nl.torquelink.shared.routing.TorqueLinkRouting

interface TorqueLinkEventRoutingV1 {
    @Resource("events")
    class Events(
        val parent: TorqueLinkRouting.Api.V1 = TorqueLinkRouting.Api.V1(),
        val page: Int? = null,
        val limit: Int? = null,
        @Contextual val filters: Filters? = null
    ){
        @Resource("{eventId}")
        class ById(
            val parent: Events = Events(),
            val eventId: Long
        ) {
            @Resource("attendees")
            class Attendees(
                val parent: ById
            ){
                @Resource("{attendeeId}")
                class ById(
                    val parent: Attendees,
                    val attendeeId: Long
                )
            }
        }
    }
}