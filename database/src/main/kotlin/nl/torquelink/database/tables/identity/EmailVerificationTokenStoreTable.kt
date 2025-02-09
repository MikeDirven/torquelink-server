package nl.torquelink.database.tables.identity

import nl.torquelink.database.interfaces.CoreTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object EmailVerificationTokenStoreTable : CoreTable("TL_AUTH_Verification_Tokens"){
    override val active: Column<Boolean> = bool("active").default(true)

    val identity = reference("identity", IdentityTable)
    val verificationToken = largeText("verificationToken")

    val verificationTokenExpireDateTime = datetime("verificationTokenExpireDateTime")
        .default(LocalDateTime.now().plusHours(6))
}