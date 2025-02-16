package nl.torquelink.database.dao.users

import nl.torquelink.database.dao.identity.IdentityDao
import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.users.UserCarsTable
import nl.torquelink.database.tables.users.UserProfileTable
import nl.torquelink.shared.enums.generic.CountryCode
import nl.torquelink.shared.models.profile.UserProfiles
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            id.value,
            if(emailIsPublic) identity.email else "****",
            if(firstNameIsPublic) firstName else "****",
            if(lastNameIsPublic) lastName else "****",
            if(dateOfBirthIsPublic) dateOfBirth.format(DateTimeFormatter.ISO_LOCAL_DATE) else LocalDate.MIN.format(DateTimeFormatter.ISO_LOCAL_DATE),
            if(phoneNumberIsPublic) phoneNumber else "****",
            if(countryIsPublic) country else CountryCode.PRIVATE,
            if(cityIsPublic) city else "****",
            avatar ?: "",
            userCars.map(UserCarDao::toResponseWithoutEngineDetails),

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
            id.value,
            if(emailIsPublic) identity.email else "****",
            if(firstNameIsPublic) firstName else "****",
            if(lastNameIsPublic) lastName else "****",
            if(dateOfBirthIsPublic) dateOfBirth.format(DateTimeFormatter.ISO_LOCAL_DATE) else LocalDate.MIN.format(DateTimeFormatter.ISO_LOCAL_DATE),
            if(phoneNumberIsPublic) phoneNumber else "****",
            if(countryIsPublic) country else CountryCode.PRIVATE,
            if(cityIsPublic) city else "****",
            avatar ?: "",
            userCars.map(UserCarDao::toResponseWithoutEngineDetails)
        )
    }
}