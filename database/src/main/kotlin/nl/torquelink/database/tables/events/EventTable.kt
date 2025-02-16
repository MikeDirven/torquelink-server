package nl.torquelink.database.tables.events

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.database.tables.groups.GroupTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime

object EventTable : CoreTable("TL_D_Events") {
    override val active: Column<Boolean> = bool("active")

    val eventTitle = varchar("eventTitle", 255)
    val eventDescription = mediumText("eventDescription")
    val eventImage = largeText("eventImage")
    val eventOrganizer = reference("eventOrganizer", GroupTable)

    val eventLocation = varchar("eventLocation", 255)
    val eventDateTime = datetime("eventDateTime")

    val eventCapacity = integer("eventCapacity")
//    val eventStatus = enumeration<EventStatus>("eventStatus")
    val eventPrivate = bool("eventPrivate").default(false)
}