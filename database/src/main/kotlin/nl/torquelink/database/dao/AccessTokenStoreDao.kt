package nl.torquelink.database.dao

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.AccessTokenStoreTable
import org.jetbrains.exposed.dao.id.EntityID

class AccessTokenStoreDao(id : EntityID<Long>) : CoreEntity(id, AccessTokenStoreTable) {
    companion object : CoreEntityClass<AccessTokenStoreDao>(AccessTokenStoreTable)

    var identity by IdentityDao referencedOn AccessTokenStoreTable.identity
    var accessToken by AccessTokenStoreTable.accessToken
    var accessTokenValid by AccessTokenStoreTable.accessTokenValid
    var refreshToken by AccessTokenStoreTable.refreshToken

    var accessTokenExpireDateTime by AccessTokenStoreTable.accessTokenExpireDateTime
    var refreshTokenExpireDateTime by AccessTokenStoreTable.refreshTokenExpireDateTime
}