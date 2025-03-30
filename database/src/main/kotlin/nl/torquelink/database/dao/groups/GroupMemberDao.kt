package nl.torquelink.database.dao.groups

import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.groups.GroupMembersTable
import nl.torquelink.shared.models.group.GroupMembers
import org.jetbrains.exposed.dao.id.EntityID

class GroupMemberDao(id : EntityID<Long>) : CoreEntity(id, GroupMembersTable) {
    companion object : CoreEntityClass<GroupMemberDao>(GroupMembersTable)

    var user by UserProfileDao referencedOn GroupMembersTable.userId
    var group by GroupDao referencedOn GroupMembersTable.groupId
    var role by GroupMembersTable.role
    var notifications by GroupMembersTable.notifications

    fun toResponse() : GroupMembers.GroupMemberDto {
        return GroupMembers.GroupMemberDto(
            user = user.toResponseWithoutSettings(),
            role = role
        )
    }
}