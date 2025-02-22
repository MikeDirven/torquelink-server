package nl.torquelink.database.dao.events

import nl.torquelink.database.dao.groups.GroupDao
import nl.torquelink.database.dao.users.UserProfileDao
import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.events.EventIntermediateTable
import nl.torquelink.database.tables.events.EventTable
import nl.torquelink.shared.models.event.Events
import org.jetbrains.exposed.dao.id.EntityID
import java.time.format.DateTimeFormatter

class EventDao(id : EntityID<Long>) : CoreEntity(id, EventTable) {
    companion object : CoreEntityClass<EventDao>(EventTable)

    val group by GroupDao referencedOn EventTable.group
    val eventAttendees by UserProfileDao via EventIntermediateTable

    val eventTitle by EventTable.eventTitle
    val eventDescription by EventTable.eventDescription
    val eventImage by EventTable.eventImage

    val eventLocation by EventTable.eventLocation
    val eventDateTime by EventTable.eventDateTime

    val eventCapacity by EventTable.eventCapacity
    val eventStatus by EventTable.eventStatus
    val privateEvent by EventTable.privateEvent

    fun toEventDto(): Events.EventDto {
        return Events.EventDto(
            id = id.value,
            eventTitle = eventTitle,
            eventDescription = eventDescription,
            eventImage = eventImage,
            eventLocation = eventLocation,
            eventDateTime = eventDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE),
            eventCapacity = eventCapacity,
            eventStatus = eventStatus,
            privateEvent = privateEvent,
        )
    }

    fun toEventWithDetailsDto(): Events.EventWithDetailsDto {
        return Events.EventWithDetailsDto(
            id = id.value,
            eventTitle = eventTitle,
            eventDescription = eventDescription,
            eventImage = eventImage,
            eventLocation = eventLocation,
            eventDateTime = eventDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE),
            eventCapacity = eventCapacity,
            eventStatus = eventStatus,
            privateEvent = privateEvent,
            group = group.toGroupDto(),
            attendees = eventAttendees.map { it.toResponseWithoutSettings() }
        )
    }
}