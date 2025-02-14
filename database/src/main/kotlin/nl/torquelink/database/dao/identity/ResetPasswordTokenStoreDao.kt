package nl.torquelink.database.dao.identity

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.identity.ResetPasswordTokenStoreTable
import org.jetbrains.exposed.dao.id.EntityID

class ResetPasswordTokenStoreDao(id : EntityID<Long>) : CoreEntity(id, ResetPasswordTokenStoreTable) {
    companion object : CoreEntityClass<ResetPasswordTokenStoreDao>(ResetPasswordTokenStoreTable)

    var identity by IdentityDao referencedOn ResetPasswordTokenStoreTable.identity
    var resetToken by ResetPasswordTokenStoreTable.resetToken
    var resetTokenExpireDateTime by ResetPasswordTokenStoreTable.resetTokenExpireDateTime
}