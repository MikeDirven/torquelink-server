package nl.torquelink.database.tables.groups

import nl.torquelink.database.interfaces.CoreTable
import org.jetbrains.exposed.sql.Column

object GroupIntermediateTable : CoreTable("TL_D_Groups_Member_Intermediate") {
    override val active: Column<Boolean> = bool("active")

    val groupMember = reference("groupMember", GroupMembersTable).index()
    val groupId = reference("groupId", GroupTable).index()

    init {
        uniqueIndex(groupMember, groupId)
    }
}