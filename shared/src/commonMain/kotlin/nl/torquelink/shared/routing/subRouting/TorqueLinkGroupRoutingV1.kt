package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*
import kotlinx.serialization.Contextual
import nl.torquelink.shared.enums.group.GroupMemberRole
import nl.torquelink.shared.filters.Filters
import nl.torquelink.shared.routing.TorqueLinkRouting

interface TorqueLinkGroupRoutingV1 {
    @Resource("groups")
    class Groups(
        val parent: TorqueLinkRouting.Api.V1 = TorqueLinkRouting.Api.V1(),
        val page: Int? = null,
        val limit: Int? = null,
        @Contextual val filters: Filters? = null
    ){
        @Resource("{groupId}")
        class ById(
            val parent: Groups = Groups(),
            val groupId: Long
        ) {
            @Resource("members")
            class Members(
                val parent: ById
            ){
                @Resource("{memberId}")
                class ById(
                    val parent: Members,
                    val memberId: Long
                ) {
                    @Resource("{action")
                    class Action(
                        val parent: ById,
                        val action: GroupMemberRole
                    )
                }
            }
        }
    }
}