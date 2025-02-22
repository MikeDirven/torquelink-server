package nl.torquelink.database.tables.events

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.database.tables.users.UserProfileTable
import org.jetbrains.exposed.sql.Column

object EventIntermediateTable : CoreTable("TL_D_Events_Intermediate") {
    override val active: Column<Boolean> = bool("active")

    val userId = reference("userId", UserProfileTable).index()
    val eventId = reference("eventId", EventTable).index()

    init {
        uniqueIndex(userId, eventId)
    }
}