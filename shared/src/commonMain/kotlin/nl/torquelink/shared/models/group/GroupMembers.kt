package nl.torquelink.shared.models.group

import kotlinx.serialization.Serializable
import nl.torquelink.shared.enums.group.GroupMemberRole
import nl.torquelink.shared.models.profile.UserProfiles

@Serializable
sealed interface GroupMembers {
    val user: UserProfiles.UserProfileDto
    val role: GroupMemberRole

    @Serializable
    data class GroupMemberDto(
        override val user: UserProfiles.UserProfileDto,
        override val role: GroupMemberRole
    ) : GroupMembers
}