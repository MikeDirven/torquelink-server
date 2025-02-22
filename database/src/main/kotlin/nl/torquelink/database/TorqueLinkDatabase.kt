package nl.torquelink.database

import com.zaxxer.hikari.HikariDataSource
import nl.torquelink.database.interfaces.DatabaseHolder
import nl.torquelink.database.tables.events.EventIntermediateTable
import nl.torquelink.database.tables.events.EventTable
import nl.torquelink.database.tables.groups.GroupIntermediateTable
import nl.torquelink.database.tables.groups.GroupMembersTable
import nl.torquelink.database.tables.groups.GroupTable
import nl.torquelink.database.tables.identity.*
import nl.torquelink.database.tables.users.UserCarsTable
import nl.torquelink.database.tables.users.UserProfileTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.EmptyCoroutineContext

object TorqueLinkDatabase : DatabaseHolder() {
    private val connection: Database by lazy {
        Database.connect(
            HikariDataSource().apply {
                jdbcUrl = "jdbc:mysql://localhost:3306/torque_link"
                driverClassName = "com.mysql.cj.jdbc.Driver"
                username = "TORQUELINK_API"
                password = "Nevr!d1579288"
                maximumPoolSize = 4
                isReadOnly = false
                transactionIsolation = "TRANSACTION_SERIALIZABLE"
            }
        )
    }

    init {
        execute {
            SchemaUtils.createMissingTablesAndColumns(
                IdentityTable,
                AccessTokenStoreTable,
                RememberTokenStoreTable,
                EmailVerificationTokenStoreTable,
                ResetPasswordTokenStoreTable,

                UserProfileTable,
                UserCarsTable,

                GroupTable,
                GroupMembersTable,
                GroupIntermediateTable,

                EventTable,
                EventIntermediateTable
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