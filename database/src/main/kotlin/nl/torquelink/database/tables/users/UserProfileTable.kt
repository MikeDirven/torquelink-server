package nl.torquelink.database.tables.users

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.database.tables.identity.IdentityTable
import nl.torquelink.shared.enums.generic.CountryCode
import org.jetbrains.exposed.sql.javatime.date

object UserProfileTable : CoreTable("TL_D_User_Profiles") {
    override val active = bool("active").default(true)
    val identity = reference("identity", IdentityTable)

    // Details
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
    val dateOfBirth = date("date_of_birth")
    val phoneNumber = varchar("phone_number", 20).nullable()
    val country = enumeration<CountryCode>("country")
    val city = varchar("city", 255).nullable()
    val avatar = largeText("avatar").nullable()
    val avatarUrl = largeText("avatarUrl").nullable()

    // Privacy settings
    val emailIsPublic = bool("emailIsPublic").default(false)
    val firstNameIsPublic = bool("first_nameIsPublic").default(true)
    val lastNameIsPublic = bool("last_nameIsPublic").default(true)
    val dateOfBirthIsPublic = bool("date_of_birth_is_public").default(true)
    val phoneNumberIsPublic = bool("phone_number_is_public").default(true)
    val countryIsPublic = bool("country_is_public").default(true)
    val cityIsPublic = bool("city_is_public").default(true)
}