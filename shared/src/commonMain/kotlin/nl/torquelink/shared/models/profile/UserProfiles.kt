package nl.torquelink.shared.models.profile

import kotlinx.serialization.Serializable
import nl.torquelink.shared.enums.generic.CountryCode

@Serializable
sealed interface UserProfiles {
    val firstName: String?
    val lastName: String?
    val dateOfBirth: String?
    val phoneNumber: String?
    val country: CountryCode?
    val city: String?

    @Serializable
    data class UserProfileCreateDto(
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: String,
        override val phoneNumber: String,
        override val country: CountryCode,
        override val city: String,
    ) : UserProfiles

    @Serializable
    data class UserProfileUpdateDto(
        override val firstName: String? = null,
        override val lastName: String? = null,
        override val dateOfBirth: String? = null,
        override val phoneNumber: String? = null,
        override val country: CountryCode? = null,
        override val city: String? = null,

        val emailIsPublic: Boolean? = null,
        val firstNameIsPublic: Boolean? = null,
        val lastNameIsPublic: Boolean? = null,
        val dateOfBirthIsPublic: Boolean? = null,
        val phoneNumberIsPublic: Boolean? = null,
        val countryIsPublic: Boolean? = null,
        val cityIsPublic: Boolean? = null
    ) : UserProfiles

    @Serializable
    data class UserProfileDto(
        val id: Long,
        val email: String,
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: String?,
        override val phoneNumber: String?,
        override val country: CountryCode?,
        override val city: String?,
        val avatar: String,
        val userCars: List<UserCars.UserCarWithoutEngineDetailsDto>
    ) : UserProfiles

    @Serializable
    data class UserProfileWithSettingsDto(
        val id: Long,
        val email: String,
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: String?,
        override val phoneNumber: String?,
        override val country: CountryCode?,
        override val city: String?,
        val avatar: String,
        val userCars: List<UserCars.UserCarWithoutEngineDetailsDto>,

        val emailIsPublic: Boolean,
        val firstNameIsPublic: Boolean,
        val lastNameIsPublic: Boolean,
        val dateOfBirthIsPublic: Boolean,
        val phoneNumberIsPublic: Boolean,
        val countryIsPublic: Boolean,
        val cityIsPublic: Boolean
    ) : UserProfiles
}
