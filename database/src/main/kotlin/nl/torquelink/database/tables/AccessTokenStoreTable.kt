package nl.torquelink.database.tables

import nl.torquelink.database.interfaces.CoreTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object AccessTokenStoreTable : CoreTable("TL_AUTH_Access_Tokens"){
    override val active: Column<Boolean> = bool("active").default(true)

    val identity = reference("identity", IdentityTable)
    val accessToken = largeText("token")
    val accessTokenValid = bool("accessTokenValid").default(true)
    val refreshToken = largeText("refreshToken")

    val accessTokenExpireDateTime = datetime("tokenExpireDateTime")
        .default(LocalDateTime.now().plusHours(3))

    val refreshTokenExpireDateTime = datetime("refreshTokenExpireDateTime")
        .default(LocalDateTime.now().plusDays(30))
}