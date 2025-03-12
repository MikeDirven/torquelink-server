package nl.torquelink.extensions

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import nl.torquelink.database.dao.identity.AccessTokenStoreDao
import nl.torquelink.database.dao.identity.IdentityDao
import nl.torquelink.database.tables.identity.AccessTokenStoreTable
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.shared.models.auth.AuthenticationResponses
import org.jetbrains.exposed.sql.and

fun RoutingContext.identity() : IdentityDao {
    val authentication = call.principal<AuthenticationResponses>()
        ?: throw AuthExceptions.NoValidTokenFound

    // get currentIdentity
    return AccessTokenStoreDao.find {
        AccessTokenStoreTable.accessToken eq authentication.accessToken and (
                AccessTokenStoreTable.active eq true
                )
    }.singleOrNull()?.identity ?: throw AuthExceptions.NoValidTokenFound
}