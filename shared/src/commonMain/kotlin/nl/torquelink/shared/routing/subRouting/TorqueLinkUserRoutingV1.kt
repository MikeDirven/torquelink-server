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
        class ByUserId(
            val parent: Profiles = Profiles(),
            val userId: Long
        ) {
            @Resource("avatar")
            class Avatar(
                val parent: ByUserId
            )
        }

        @Resource("cars")
        class Cars(
            val parent: Profiles = Profiles()
        ){
            @Resource("{carId}")
            class ByCarId(
                val parent: Cars = Cars(),
                val carId: Long
            ) {
                @Resource("photos")
                class Photos(
                    val parent: ByCarId
                ){
                    @Resource("{photoId}")
                    class Photo(
                        val parent: Photos,
                        val photoId: Long
                    )
                }
            }
        }
    }
}