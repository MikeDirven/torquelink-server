package nl.torquelink.database.tables

import nl.torquelink.database.interfaces.CoreTable
import org.jetbrains.exposed.sql.Column

object RememberTokenStoreTable : CoreTable("TL_AUTH_Remember_Tokens"){
    override val active: Column<Boolean> = bool("active").default(true)

    val identity = reference("identity", IdentityTable)
    val token = largeText("token")
}