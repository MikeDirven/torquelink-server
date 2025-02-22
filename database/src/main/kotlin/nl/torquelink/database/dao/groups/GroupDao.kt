package nl.torquelink.database.dao.groups

import nl.torquelink.database.dao.events.EventDao
import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.events.EventTable
import nl.torquelink.database.tables.groups.GroupIntermediateTable
import nl.torquelink.database.tables.groups.GroupTable
import nl.torquelink.shared.models.group.Groups
import org.jetbrains.exposed.dao.id.EntityID

class GroupDao(id : EntityID<Long>) : CoreEntity(id, GroupTable) {
    companion object : CoreEntityClass<GroupDao>(GroupTable)

    val groupName by GroupTable.groupName
    val description by GroupTable.description

    val logoUrl by GroupTable.logoUrl
    val coverPhotoUrl by GroupTable.coverPhotoUrl
    val privateGroup by GroupTable.privateGroup
    val joinRequestsEnabled by GroupTable.joinRequestsEnabled
    val memberListVisibility by GroupTable.memberListVisibility

    val facebookUrl by GroupTable.facebookUrl
    val twitterUrl by GroupTable.twitterUrl
    val instagramUrl by GroupTable.instagramUrl
    val linkedInUrl by GroupTable.linkedInUrl
    val websiteUrl by GroupTable.websiteUrl

    val members by GroupMemberDao via GroupIntermediateTable
    val events by EventDao referrersOn EventTable.group

    fun toGroupDto() : Groups.GroupDto {
        return Groups.GroupDto(
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