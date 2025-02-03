package nl.torquelink.database.dao.users

import nl.torquelink.database.dao.identity.IdentityDao
import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.users.UserCarsTable
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.shared.models.profile.UserProfiles
import org.jetbrains.exposed.dao.id.EntityID

class UserProfileDao(id : EntityID<Long>) : CoreEntity(id, UserProfileTable) {
    companion object : CoreEntityClass<UserProfileDao>(UserProfileTable)

    var identity by IdentityDao referencedOn UserProfileTable.identity

    // Profile
    var firstName by UserProfileTable.firstName
    var lastName by UserProfileTable.lastName
    var dateOfBirth by UserProfileTable.dateOfBirth
    var phoneNumber by UserProfileTable.phoneNumber
    var country by UserProfileTable.country
    var city by UserProfileTable.city
    var avatar by UserProfileTable.avatar

    // References
    val userCars by UserCarDao referrersOn UserCarsTable.user

    // Settings
    var emailIsPublic by UserProfileTable.emailIsPublic
    var firstNameIsPublic by UserProfileTable.firstNameIsPublic
    var lastNameIsPublic by UserProfileTable.lastNameIsPublic
    var dateOfBirthIsPublic by UserProfileTable.dateOfBirthIsPublic
    var phoneNumberIsPublic by UserProfileTable.phoneNumberIsPublic
    var countryIsPublic by UserProfileTable.countryIsPublic
    var cityIsPublic by UserProfileTable.cityIsPublic

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