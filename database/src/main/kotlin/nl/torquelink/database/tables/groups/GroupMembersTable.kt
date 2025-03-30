package nl.torquelink.database.tables.groups

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.shared.enums.group.GroupMemberRole
import org.jetbrains.exposed.sql.Column

object GroupMembersTable : CoreTable("TL_D_Groups_Members") {
    override val active: Column<Boolean> = bool("active").default(true)

    val groupId = reference("groupId", GroupTable).index()
    val userId = reference("userId", UserProfileTable).index()
    val role = enumeration<GroupMemberRole>("role").index()
    val notifications = bool("notifications").index()

    init {
        uniqueIndex(groupId, userId)
    }
}