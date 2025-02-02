package nl.torquelink.shared.models.profile

import kotlinx.serialization.Serializable
import nl.torquelink.shared.enums.CountryCode
import nl.torquelink.shared.serializers.LocalDateSerializer
import java.time.LocalDate

@Serializable
sealed interface UserProfiles {
    val firstName: String
    val lastName: String
    @Serializable(with = LocalDateSerializer::class) val datOfBirth: LocalDate?
    val phoneNumber: String?
    val country: CountryCode?
    val city: String?
    val avatar: String

    @Serializable
    data class UserProfileDto(
        override val firstName: String,
        override val lastName: String,
        @Serializable(with = LocalDateSerializer::class) override val datOfBirth: LocalDate?,
        override val phoneNumber: String?,
        override val country: CountryCode?,
        override val city: String?,
        override val avatar: String
    ) : UserProfiles

    @Serializable
    data class UserProfileWithSettingsDto(
        override val firstName: String,
        override val lastName: String,
        @Serializable(with = LocalDateSerializer::class) override val datOfBirth: LocalDate?,
        override val phoneNumber: String?,
        override val country: CountryCode?,
        override val city: String?,
        override val avatar: String,

        val emailIsPublic: Boolean,
        val firstNameIsPublic: Boolean,
        val lastNameIsPublic: Boolean,
        val datOfBirthIsPublic: Boolean,
        val phoneNumberIsPublic: Boolean,
        val countryIsPublic: Boolean,
        val cityIsPublic: Boolean
    ) : UserProfiles
}
