package nl.torquelink.database

import nl.torquelink.database.interfaces.DatabaseHolder
import nl.torquelink.database.tables.identity.*
import nl.torquelink.database.tables.users.UserCarsTable
import nl.torquelink.database.tables.users.UserProfileTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.EmptyCoroutineContext

object TorqueLinkDatabase : DatabaseHolder(){
    private val connection: Database by lazy {
        Database.connect(
            url = "jdbc:mysql://localhost:3306/torque_link",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "TORQUELINK_API",
            password = "Nevr!d1579288"
        )
    }

    init {
        execute {
            SchemaUtils.create(
                IdentityTable,
                AccessTokenStoreTable,
                RememberTokenStoreTable,
                EmailVerificationTokenStoreTable,
                ResetPasswordTokenStoreTable,

                UserProfileTable,
                UserCarsTable
            )
        }
    }

    override fun <T> execute(execution: () -> T) : T {
        return transaction(connection) {
            execution()
        }
    }

    override suspend fun <T> executeAsync(execution: suspend () -> T) : T {
        return newSuspendedTransaction(EmptyCoroutineContext, connection) {
            execution()
        }
    }
}