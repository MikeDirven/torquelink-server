package nl.torquelink.database.tables.groups

import nl.torquelink.database.tables.users.UserProfileTable
import org.jetbrains.exposed.sql.Table

object GroupIntermediateTable : Table("TL_D_Groups_Member_Intermediate") {
    val groupMember = reference("groupMember", GroupMembersTable).index()
    val groupId = reference("groupId", GroupTable).index()
    val userId = reference("userId", UserProfileTable).index()
    override val primaryKey: PrimaryKey = PrimaryKey(groupMember, groupId,  name = "PK_TL_D_Groups_Member_Intermediate")

    init {
        uniqueIndex(groupMember, groupId)
    }
}