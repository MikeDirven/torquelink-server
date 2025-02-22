package nl.torquelink.database.tables.events

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.database.tables.groups.GroupTable
import nl.torquelink.shared.enums.event.EventStatus
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime

object EventTable : CoreTable("TL_D_Events") {
    override val active: Column<Boolean> = bool("active")
    val group = reference("group", GroupTable)

    val eventTitle = varchar("eventTitle", 255)
    val eventDescription = mediumText("eventDescription")
    val eventImage = largeText("eventImage")

    val eventLocation = varchar("eventLocation", 255)
    val eventDateTime = datetime("eventDateTime")

    val eventCapacity = integer("eventCapacity")
    val eventStatus = enumeration<EventStatus>("eventStatus")
    val privateEvent = bool("privateEvent").default(false)
}