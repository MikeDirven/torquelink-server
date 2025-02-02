package nl.torquelink.database.dao

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.RememberTokenStoreTable
import org.jetbrains.exposed.dao.id.EntityID

class RememberTokenStoreDao(id : EntityID<Long>) : CoreEntity(id, RememberTokenStoreTable) {
    companion object : CoreEntityClass<RememberTokenStoreDao>(RememberTokenStoreTable)

    var identity by IdentityDao referencedOn RememberTokenStoreTable.identity
    var token by RememberTokenStoreTable.token
}