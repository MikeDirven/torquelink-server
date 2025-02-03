package nl.torquelink.database.tables.identity

import nl.torquelink.database.config.CipherConfig
import nl.torquelink.database.interfaces.IdentityTable
import org.jetbrains.exposed.crypt.Algorithms
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object IdentityTable : IdentityTable("TL_AUTH_Identities"){
    override val active: Column<Boolean> = bool("active").default(true)

    val username = varchar("username", 100)
    val email = encryptedVarchar(
        "email",
        255,
        Algorithms.AES_256_PBE_CBC(CipherConfig.SECRET_KEY, CipherConfig.SALT)
    ).uniqueIndex()
    val passwordHash = encryptedVarchar(
        "password_hash",
        255,
        Algorithms.AES_256_PBE_CBC(CipherConfig.SECRET_KEY, CipherConfig.SALT)
    )

    val isEmailConfirmed = bool("isEmailConfirmed").default(false)
    val registrationDate = datetime("registrationDate").default(LocalDateTime.now())
    val lastLoginDate = datetime("lastLoginDate").default(LocalDateTime.now())
    val lastFailedLoginDate = datetime("lastFailedLoginDate").nullable()
    val failedLoginAttempts = integer("failedLoginAttempts").default(0)
    val isLockedOut = bool("isLockedOut").default(false)
    val lockoutEndDate = datetime("lockedOutEndDate").nullable()
    val rememberTokens = largeText("rememberTokens").transform(
        wrap = {
            it.split(",")
        },
        unwrap = {
            it.joinToString(",")
        }
    )

    init {
        index(false, registrationDate)
        index(false, isEmailConfirmed)
        index(false, isLockedOut)
    }
}