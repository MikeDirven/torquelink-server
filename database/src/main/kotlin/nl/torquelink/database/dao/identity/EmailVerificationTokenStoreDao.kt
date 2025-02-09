package nl.torquelink.database.dao.identity

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.identity.EmailVerificationTokenStoreTable
import org.jetbrains.exposed.dao.id.EntityID

class EmailVerificationTokenStoreDao(id : EntityID<Long>) : CoreEntity(id, EmailVerificationTokenStoreTable) {
    companion object : CoreEntityClass<EmailVerificationTokenStoreDao>(EmailVerificationTokenStoreTable)

    var identity by IdentityDao referencedOn EmailVerificationTokenStoreTable.identity
    var verificationToken by EmailVerificationTokenStoreTable.verificationToken
    var verificationTokenExpireDateTime by EmailVerificationTokenStoreTable.verificationTokenExpireDateTime
}