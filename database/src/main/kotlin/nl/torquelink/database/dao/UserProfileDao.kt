package nl.torquelink.database.dao

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.UserCarsTable
import nl.torquelink.database.tables.UserProfileTable
import nl.torquelink.shared.models.profile.UserProfiles
import org.jetbrains.exposed.dao.id.EntityID

class UserProfileDao(id : EntityID<Long>) : CoreEntity(id, UserProfileTable) {
    companion object : CoreEntityClass<UserProfileDao>(UserProfileTable)

    val identity by IdentityDao referencedOn UserProfileTable.identity

    // Profile
    val firstName by UserProfileTable.firstName
    val lastName by UserProfileTable.lastName
    val dateOfBirth by UserProfileTable.dateOfBirth
    val phoneNumber by UserProfileTable.phoneNumber
    val country by UserProfileTable.country
    val city by UserProfileTable.city
    val avatar by UserProfileTable.avatar

    // References
    val userCars by UserCarDao referrersOn UserCarsTable.user

    // Settings
    val emailIsPublic by UserProfileTable.emailIsPublic
    val firstNameIsPublic by UserProfileTable.firstNameIsPublic
    val lastNameIsPublic by UserProfileTable.lastNameIsPublic
    val dateOfBirthIsPublic by UserProfileTable.dateOfBirthIsPublic
    val phoneNumberIsPublic by UserProfileTable.phoneNumberIsPublic
    val countryIsPublic by UserProfileTable.countryIsPublic
    val cityIsPublic by UserProfileTable.cityIsPublic

    fun toResponseWithSettings() : UserProfiles.UserProfileWithSettingsDto {
        return UserProfiles.UserProfileWithSettingsDto(
            firstName,
            lastName,
            dateOfBirth,
            phoneNumber,
            country,
            city,
            avatar ?: "",
            emailIsPublic,
            firstNameIsPublic,
            lastNameIsPublic,
            dateOfBirthIsPublic,
            phoneNumberIsPublic,
            countryIsPublic,
            cityIsPublic
        )
    }

    fun toResponseWithoutSettings() : UserProfiles.UserProfileDto {
        return UserProfiles.UserProfileDto(
            firstName,
            lastName,
            dateOfBirth,
            phoneNumber,
            country,
            city,
            avatar ?: ""
        )
    }
}