package nl.torquelink.shared.models.group

import kotlinx.serialization.Serializable
import nl.torquelink.shared.enums.group.MemberListVisibility
import nl.torquelink.shared.models.event.Events

@Serializable
sealed interface Groups {
    val groupName: String?
    val description: String?
    val logoUrl: String?
    val coverPhotoUrl: String?
    val privateGroup: Boolean?
    val joinRequestsEnabled: Boolean?
    val memberListVisibility: MemberListVisibility?
    val facebookUrl: String?
    val instagramUrl: String?
    val twitterUrl: String?
    val linkedInUrl: String?
    val websiteUrl: String?

    @Serializable
    data class GroupDto(
        val id: Long,
        override val groupName: String,
        override val description: String?,
        override val logoUrl: String?,
        override val coverPhotoUrl: String?,
        override val privateGroup: Boolean,
        override val joinRequestsEnabled: Boolean,
        override val memberListVisibility: MemberListVisibility,
        override val facebookUrl: String?,
        override val instagramUrl: String?,
        override val twitterUrl: String?,
        override val linkedInUrl: String?,
        override val websiteUrl: String?
    ) : Groups

    @Serializable
    data class GroupWithDetailsDto(
        val id: Long,
        val members: List<GroupMembers.GroupMemberDto>,
        val events: List<Events.EventDto>,
        override val groupName: String,
        override val description: String?,
        override val logoUrl: String?,
        override val coverPhotoUrl: String?,
        override val privateGroup: Boolean,
        override val joinRequestsEnabled: Boolean,
        override val memberListVisibility: MemberListVisibility,
        override val facebookUrl: String?,
        override val instagramUrl: String?,
        override val twitterUrl: String?,
        override val linkedInUrl: String?,
        override val websiteUrl: String?
    ): Groups

    @Serializable
    data class GroupCreateDto(
        override val groupName: String,
        override val description: String,
        override val logoUrl: String?,
        override val coverPhotoUrl: String?,
        override val privateGroup: Boolean = false,
        override val joinRequestsEnabled: Boolean = true,
        override val memberListVisibility: MemberListVisibility = MemberListVisibility.VISIBLE,
        override val facebookUrl: String?,
        override val instagramUrl: String?,
        override val twitterUrl: String?,
        override val linkedInUrl: String?,
        override val websiteUrl: String?
    ) : Groups

    @Serializable
    data class GroupUpdateDto(
        val members: List<GroupMembers.GroupMemberDto>? = null,
        val events: List<Events.EventDto>? = null,
        override val groupName: String? = null,
        override val description: String? = null,
        override val logoUrl: String? = null,
        override val coverPhotoUrl: String? = null,
        override val privateGroup: Boolean? = null,
        override val joinRequestsEnabled: Boolean? = null,
        override val memberListVisibility: MemberListVisibility? = null,
        override val facebookUrl: String? = null,
        override val instagramUrl: String? = null,
        override val twitterUrl: String? = null,
        override val linkedInUrl: String? = null,
        override val websiteUrl: String? = null
    ) : Groups
}