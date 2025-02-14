package nl.torquelink.database.tables.identity

import nl.torquelink.database.interfaces.CoreTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ResetPasswordTokenStoreTable : CoreTable("TL_AUTH_Reset_Tokens"){
    override val active: Column<Boolean> = bool("active").default(true)

    val identity = reference("identity", IdentityTable)
    val resetToken = largeText("resetToken")

    val resetTokenExpireDateTime = datetime("resetTokenExpireDateTime")
        .default(LocalDateTime.now().plusHours(3))
}