package nl.torquelink.database.tables.identity

import nl.torquelink.database.interfaces.CoreTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object RememberTokenStoreTable : CoreTable("TL_AUTH_Remember_Tokens"){
    override val active: Column<Boolean> = bool("active").default(true)

    val identity = reference("identity", IdentityTable)
    val token = largeText("token")
    val expirationDate = date("expirationDate").default(LocalDate.now().plusYears(1))
}