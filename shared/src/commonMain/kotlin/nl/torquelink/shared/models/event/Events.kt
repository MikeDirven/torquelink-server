package nl.torquelink.shared.models.event

import kotlinx.serialization.Serializable
import nl.torquelink.shared.enums.event.EventStatus
import nl.torquelink.shared.models.group.Groups
import nl.torquelink.shared.models.profile.UserProfiles

@Serializable
sealed interface Events {
    val eventTitle: String?
    val eventDescription: String?
    val eventImage: String?
    val eventLocation: String?
    val eventDateTime: String?
    val eventCapacity: Int?
    val eventStatus: EventStatus?
    val privateEvent: Boolean?

    @Serializable
    data class EventDto(
        val id: Long,
        override val eventTitle: String,
        override val eventDescription: String,
        override val eventImage: String,
        override val eventLocation: String,
        override val eventDateTime: String,
        override val eventCapacity: Int,
        override val eventStatus: EventStatus,
        override val privateEvent: Boolean
    ) : Events

    @Serializable
    data class EventWithDetailsDto(
        val id: Long,
        val group: Groups,
        val attendees: List<UserProfiles.UserProfileDto>,
        override val eventTitle: String,
        override val eventDescription: String,
        override val eventImage: String,
        override val eventLocation: String,
        override val eventDateTime: String,
        override val eventCapacity: Int,
        override val eventStatus: EventStatus,
        override val privateEvent: Boolean
    ) : Events

    @Serializable
    data class EventCreateDto(
        override val eventTitle: String,
        override val eventDescription: String,
        override val eventImage: String,
        override val eventLocation: String,
        override val eventDateTime: String,
        override val eventCapacity: Int,
        override val privateEvent: Boolean
    ) : Events {
        override val eventStatus: EventStatus = EventStatus.CREATED
    }

    @Serializable
    data class EventUpdateDto(
        override val eventTitle: String? = null,
        override val eventDescription: String? = null,
        override val eventImage: String? = null,
        override val eventLocation: String? = null,
        override val eventDateTime: String? = null,
        override val eventCapacity: Int? = null,
        override val eventStatus: EventStatus? = null,
        override val privateEvent: Boolean? = null
    ) : Events
}