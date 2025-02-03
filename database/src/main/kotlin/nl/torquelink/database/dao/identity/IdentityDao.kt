package nl.torquelink.database.dao.identity

import nl.torquelink.database.interfaces.IdentityEntity
import nl.torquelink.database.interfaces.IdentityEntityClass
import nl.torquelink.database.tables.identity.IdentityTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class IdentityDao(id : EntityID<UUID>) : IdentityEntity(id, IdentityTable) {
    companion object : IdentityEntityClass<IdentityDao>(IdentityTable)

    var username by IdentityTable.username
    var email by IdentityTable.email
    var passwordHash by IdentityTable.passwordHash

    var isEmailConfirmed by IdentityTable.isEmailConfirmed
    var registrationDate by IdentityTable.registrationDate
    var lastLoginDate by IdentityTable.lastLoginDate
    var lastFailedLoginDate by IdentityTable.lastFailedLoginDate
    var failedLoginAttempts by IdentityTable.failedLoginAttempts
    var isLockedOut by IdentityTable.isLockedOut
    var lockoutEndDate by IdentityTable.lockoutEndDate
    var rememberTokens by IdentityTable.rememberTokens
}