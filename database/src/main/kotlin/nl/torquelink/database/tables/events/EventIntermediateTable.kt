package nl.torquelink.database.tables.events

import nl.torquelink.database.tables.users.UserProfileTable
import org.jetbrains.exposed.sql.Table

object EventIntermediateTable : Table("TL_D_Events_Intermediate") {
    val userId = reference("userId", UserProfileTable).index()
    val eventId = reference("eventId", EventTable).index()

    override val primaryKey: PrimaryKey = PrimaryKey(userId, eventId, name = "PK_TL_D_Events_Intermediate")

    init {
        uniqueIndex(userId, eventId)
    }
}