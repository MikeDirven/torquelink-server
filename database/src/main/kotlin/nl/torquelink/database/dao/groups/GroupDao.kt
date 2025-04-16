package nl.torquelink.database.dao.groups

import nl.torquelink.database.dao.events.EventDao
import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.events.EventTable
import nl.torquelink.database.tables.groups.GroupIntermediateTable
import nl.torquelink.database.tables.groups.GroupTable
import nl.torquelink.shared.enums.group.GroupMemberRole
import nl.torquelink.shared.models.group.Groups
import org.jetbrains.exposed.dao.id.EntityID

class GroupDao(id : EntityID<Long>) : CoreEntity(id, GroupTable) {
    companion object : CoreEntityClass<GroupDao>(GroupTable)

    var groupName by GroupTable.groupName
    var description by GroupTable.description

    var logoUrl by GroupTable.logoUrl
    var coverPhotoUrl by GroupTable.coverPhotoUrl
    var privateGroup by GroupTable.privateGroup
    var joinRequestsEnabled by GroupTable.joinRequestsEnabled
    var memberListVisibility by GroupTable.memberListVisibility

    var facebookUrl by GroupTable.facebookUrl
    var twitterUrl by GroupTable.twitterUrl
    var instagramUrl by GroupTable.instagramUrl
    var linkedInUrl by GroupTable.linkedInUrl
    var websiteUrl by GroupTable.websiteUrl

    var members by GroupMemberDao via GroupIntermediateTable
    val events by EventDao referrersOn EventTable.group

    fun toGroupDto() : Groups.GroupDto {
        return Groups.GroupDto(
            id = this.id.value,
            memberCount = this.members.count { dao ->
                dao.role in listOf(GroupMemberRole.MEMBER, GroupMemberRole.ADMIN)
            }.toLong(),
            followerCount = this.members.count { dao ->
                dao.role == GroupMemberRole.FOLLOWER
            }.toLong(),
            eventCount = this.events.count(),
            groupName = this.groupName,
            description = this.description,
            logoUrl = this.logoUrl,
            coverPhotoUrl = this.coverPhotoUrl,
            privateGroup = this.privateGroup,
            joinRequestsEnabled = this.joinRequestsEnabled,
            memberListVisibility = this.memberListVisibility,
            facebookUrl = this.facebookUrl,
            twitterUrl = this.twitterUrl,
            instagramUrl = this.instagramUrl,
            linkedInUrl = this.linkedInUrl,
            websiteUrl = this.websiteUrl
        )
    }

    fun toGroupWithDetailsDto() : Groups.GroupWithDetailsDto {
        return Groups.GroupWithDetailsDto(
            id = this.id.value,
            groupName = this.groupName,
            description = this.description,
            logoUrl = this.logoUrl,
            coverPhotoUrl = this.coverPhotoUrl,
            privateGroup = this.privateGroup,
            joinRequestsEnabled = this.joinRequestsEnabled,
            memberListVisibility = this.memberListVisibility,
            facebookUrl = this.facebookUrl,
            twitterUrl = this.twitterUrl,
            instagramUrl = this.instagramUrl,
            linkedInUrl = this.linkedInUrl,
            websiteUrl = this.websiteUrl,
            members = this.members.map { it.toResponse() },
            events = this.events.map { it.toEventDto() }
        )
    }
}