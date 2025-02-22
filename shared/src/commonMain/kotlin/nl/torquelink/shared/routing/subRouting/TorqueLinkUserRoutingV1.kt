package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*
import kotlinx.serialization.Contextual
import nl.torquelink.shared.filters.Filters
import nl.torquelink.shared.routing.TorqueLinkRouting

interface TorqueLinkUserRoutingV1 {
    @Resource("profiles")
    class Profiles(
        val parent: TorqueLinkRouting.Api.V1 = TorqueLinkRouting.Api.V1(),
        val page: Int? = null,
        val limit: Int? = null,
        @Contextual val filters: Filters? = null
    ){
        @Resource("{userId}")
        class ByUseId(
            val parent: Profiles = Profiles(),
            val userId: Long
        )

        @Resource("cars")
        class Cars(
            val parent: Profiles = Profiles()
        ){
            @Resource("{carId}")
            class ByCarId(
                val parent: Cars = Cars(),
                val carId: Long
            )
        }
    }
}