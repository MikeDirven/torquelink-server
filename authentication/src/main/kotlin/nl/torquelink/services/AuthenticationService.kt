package nl.torquelink.services

import io.ktor.server.application.*
import nl.torquelink.database.dao.identity.*
import nl.torquelink.database.interfaces.DatabaseHolder
import nl.torquelink.database.tables.identity.*
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.interfaces.TokenGenerator
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.auth.RegistrationRequests
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import org.jetbrains.exposed.sql.or
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.sql.SQLException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class AuthenticationService internal constructor (
    private val application: Application,
    private val database: DatabaseHolder,
    private val tokenGenerator: TokenGenerator
) {
    init {
        instance.set(this)
    }
    private fun getVerificationEmailBody(username: String, verificationToken: String) : String {
        val stream = AuthenticationService::class.java.getResourceAsStream("/verification.html")

        return stream?.let {
            InputStreamReader(it, StandardCharsets.UTF_8).use { reader ->
                reader.readText()
            }.replace(
                "%username%",
                username
            ).replace(
                "%verificationUrl%",
                "http://torquelink.nl/auth/email/verify?verification=$verificationToken"
            )
        } ?: throw AuthExceptions.UnableToCreateEmailVerification
    }

    private fun getPasswordResetEmailBody(username: String, resetToken: String) : String {
        val stream = AuthenticationService::class.java.getResourceAsStream("/pass_reset.html")

        return stream?.let {
            InputStreamReader(it, StandardCharsets.UTF_8).use { reader ->
                reader.readText()
            }.replace(
                "%username%",
                username
            ).replace(
                "%resetUrl%",
                "http://torquelink.nl/auth/password/reset?resetToken=$resetToken"
            )
        } ?: throw AuthExceptions.UnableToCreateEmailVerification
    }

    private suspend fun IdentityDao.sendVerificationEmail() {
        try {
            val email = HtmlEmail().apply {
                setHostName("smtp.strato.com") // Voorbeeld: Gmail SMTP server
                setSmtpPort(587) // Voorbeeld: Gmail SMTP poort
                setAuthenticator(
                    DefaultAuthenticator(
                        "info@torquelink.nl",
                        "Nevr!d1579288"
                    )
                )
                isStartTLSEnabled = true
                setFrom("info@torquelink.nl")
                addTo(this@sendVerificationEmail.email)
                setSubject("Torque Link verification")
                setHtmlMsg(
                    getVerificationEmailBody(
                        this@sendVerificationEmail.username,
                        this@sendVerificationEmail.generatorVerificationToken().verificationToken
                    )
                )
            }

            email.send()
            println("Email sent successfully!")

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw AuthExceptions.UnableToCreateEmailVerification
        }
    }

    private suspend fun IdentityDao.sendPasswordResetEmail() {
        try {
            val email = HtmlEmail().apply {
                setHostName("smtp.strato.com") // Voorbeeld: Gmail SMTP server
                setSmtpPort(587) // Voorbeeld: Gmail SMTP poort
                setAuthenticator(
                    DefaultAuthenticator(
                        "info@torquelink.nl",
                        "Nevr!d1579288"
                    )
                )
                isStartTLSEnabled = true
                setFrom("info@torquelink.nl")
                addTo(this@sendPasswordResetEmail.email)
                setSubject("Torque Link password reset")
                setHtmlMsg(
                    getPasswordResetEmailBody(
                        this@sendPasswordResetEmail.username,
                        this@sendPasswordResetEmail.generatorResetPasswordToken().resetToken
                    )
                )
            }

            email.send()
            println("Email sent successfully!")

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw AuthExceptions.UnableToCreateEmailVerification
        }
    }

    private suspend fun RegistrationRequests.RegisterWithTorqueLinkDto.registerNewIdentityFromTorqueLink() : IdentityDao{
        return try {
            IdentityDao.new {
                username = this@registerNewIdentityFromTorqueLink.username
                email = this@registerNewIdentityFromTorqueLink.email
                passwordHash = this@registerNewIdentityFromTorqueLink.password
                rememberTokens = emptyList()
            }.apply {
                sendVerificationEmail()
            }
        } catch (exception: SQLException) {
            exception.printStackTrace()
            throw Exception()
        }
    }

    private fun IdentityDao.setLockedOut() {
        lastFailedLoginDate = LocalDateTime.now()
        failedLoginAttempts = failedLoginAttempts + 1
        if (failedLoginAttempts >= 3) {
            isLockedOut = true
            lockoutEndDate = LocalDateTime.now().plusHours((failedLoginAttempts * 2).toLong())
        }
    }

    private fun IdentityDao.resetLockedOut() {
        isLockedOut = false
        lockoutEndDate = null
        lastFailedLoginDate = null
    }

    private fun IdentityDao.handleLoadedAuthenticationRequest(password: String) {
        try {
            // Password check
            if (passwordHash != password)
                throw AuthExceptions.InvalidCredentials

            // Email verification check
            if(!isEmailConfirmed)
                throw AuthExceptions.EmailNotVerified

            // Last login update

            // Lock out check
            lockoutEndDate?.let {
                when {
                    isLockedOut && it > LocalDateTime.now() ->
                        AuthExceptions.AuthLockedOut(it)

                    isLockedOut && it <= LocalDateTime.now() ->
                        resetLockedOut()
                }

                return
            }

            // Catch any not correctly locked out users and unlock them for the sake of the world.
            resetLockedOut()

            return
        } catch (exception: AuthExceptions){
            // Update failed login attempts and lock out if necessary
            setLockedOut()
            throw exception
        }
    }

    private fun IdentityDao.generatorVerificationToken() : EmailVerificationTokenStoreDao {
        return EmailVerificationTokenStoreDao.new {
            identity = this@generatorVerificationToken
            verificationToken = tokenGenerator.generateVerificationToken(this@generatorVerificationToken.username)
        }
    }

    private fun IdentityDao.generatorResetPasswordToken() : ResetPasswordTokenStoreDao {
        return ResetPasswordTokenStoreDao.new {
            identity = this@generatorResetPasswordToken
            resetToken = tokenGenerator.generateVerificationToken(this@generatorResetPasswordToken.username)
        }
    }

    private fun IdentityDao.generateTokens(remember: Boolean = false) : AuthenticationResponses{
        val rememberToken = if(remember) {
            RememberTokenStoreDao.new {
                identity = this@generateTokens // Set the identity
                token = tokenGenerator.generateRememberToken(username)
            }
        } else null

        val accessToken = AccessTokenStoreDao.new {
            identity = this@generateTokens
            accessToken = tokenGenerator.generateAccessToken(username)
            refreshToken = tokenGenerator.generateRefreshToken(username)
        }

        return rememberToken?.let { rememberTokenHolder ->
                AuthenticationResponses.AuthenticationResponseWithRemember(
                    accessToken = accessToken.accessToken,
                    refreshToken = accessToken.refreshToken,
                    rememberToken = rememberTokenHolder.token
                )
            } ?: AuthenticationResponses.AuthenticationResponseDefault(
                accessToken = accessToken.accessToken,
                refreshToken = accessToken.refreshToken
            )
    }

    private fun AccessTokenStoreDao.checkAccessTokenValidation() : AuthenticationResponses {
        if(accessTokenExpireDateTime <= LocalDateTime.now()) {
            accessTokenValid = false
            throw AuthExceptions.AccessTokenInvalided
        } else {
            accessTokenExpireDateTime = LocalDateTime.now().plusHours(3)

            return AuthenticationResponses.AuthenticationResponseDefault(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    private fun AccessTokenStoreDao.checkRefreshTokenValidation() : AuthenticationResponses {
        if(refreshTokenExpireDateTime <= LocalDateTime.now()) {
            delete()
            throw AuthExceptions.RefreshTokenInvalided
        } else {
            accessToken = tokenGenerator.generateAccessToken(identity.username)
            accessTokenExpireDateTime = LocalDateTime.now().plusHours(3)
            refreshTokenExpireDateTime = LocalDateTime.now().plusDays(30)

            return AuthenticationResponses.AuthenticationResponseDefault(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    private fun EmailVerificationTokenStoreDao.handleVerification() {
        if(verificationTokenExpireDateTime < LocalDateTime.now())
            throw AuthExceptions.EmailVerificationTokenExpired

        delete()

        // Update identity variable
        identity.isEmailConfirmed = true
    }

    suspend fun loginByUsername(username: String, password: String, remember: Boolean = false) : Pair<IdentityDao, AuthenticationResponses> {
        return database.executeAsync {
            val identity = IdentityDao.find {
                IdentityTable.username eq username
            }.singleOrNull() ?: throw AuthExceptions.UserNotFoundWithUsername(username)

            identity.handleLoadedAuthenticationRequest(password)

            identity to identity.generateTokens(remember)
        }
    }

    suspend fun loginByEmail(email: String, password: String, remember: Boolean = false) : Pair<IdentityDao, AuthenticationResponses> {
        return database.executeAsync {
            val identity = IdentityDao.find {
                IdentityTable.email eq email
            }.singleOrNull() ?: throw AuthExceptions.UserNotFoundWithEmail(email)

            identity.handleLoadedAuthenticationRequest(password)

            identity to identity.generateTokens(remember)
        }
    }

    suspend fun loginByRememberToken(token: String) : Pair<IdentityDao, AuthenticationResponses> {
        return database.executeAsync {
            val rememberToken = RememberTokenStoreDao.find {
                RememberTokenStoreTable.token eq token
            }.singleOrNull() ?: throw AuthExceptions.RememberTokenInvalided

            // Check expiration date
            if(rememberToken.expirationDate < LocalDate.now())
                throw AuthExceptions.RememberTokenInvalided

            rememberToken.identity.handleLoadedAuthenticationRequest(
                rememberToken.identity.passwordHash
            )

            // Update expiration date
            rememberToken.expirationDate = LocalDate.now().plusYears(1)

            rememberToken.identity to rememberToken.identity.generateTokens(true)
        }
    }

    suspend fun checkTokenValidation(token: String) : AuthenticationResponses {
        return database.executeAsync {
            val tokenEntity = AccessTokenStoreDao.find {
                (AccessTokenStoreTable.accessToken eq token) or (AccessTokenStoreTable.refreshToken eq token)
            }.singleOrNull()?: throw AuthExceptions.NoValidTokenFound

            when (token) {
                tokenEntity.accessToken -> tokenEntity.checkAccessTokenValidation()
                tokenEntity.refreshToken -> tokenEntity.checkRefreshTokenValidation()
                else -> throw AuthExceptions.NoValidTokenFound
            }
        }
    }

    suspend fun registerNewIdentity(registration: RegistrationRequests) {
        database.executeAsync {
            when (registration) {
                is RegistrationRequests.RegisterWithTorqueLinkDto -> registration.registerNewIdentityFromTorqueLink()
            }
        }
    }

    suspend fun verifyEmail(verificationToken: String) {
        database.executeAsync {
            val verification = EmailVerificationTokenStoreDao.find {
                EmailVerificationTokenStoreTable.verificationToken eq verificationToken
            }.singleOrNull()?: throw AuthExceptions.EmailVerificationTokenNotFound

            verification.handleVerification()
        }
    }

    suspend fun createPasswordResetRequest(username: String, email: String) {
        database.executeAsync {
            val identity = IdentityDao.find {
                (IdentityTable.username eq username) or (IdentityTable.email eq email)
            }.singleOrNull()?: throw AuthExceptions.UserNotFound(username)

            identity.sendPasswordResetEmail()
        }
    }

    suspend fun resetPassword(token: String, newPassword: String) {
        database.executeAsync {
            val resetToken = ResetPasswordTokenStoreDao.find {
                ResetPasswordTokenStoreTable.resetToken eq token
            }.singleOrNull()
                ?: throw AuthExceptions.ResetPasswordTokenInvalid

            resetToken.identity.apply {
                passwordHash = newPassword
            }
        }
    }

    suspend fun setNotificationToken(identityId: UUID, token: String) {
        database.execute() {
            val identity = IdentityDao.get(identityId)

            identity.notificationToken = token
        }
    }

    companion object {
        val instance = AtomicReference<AuthenticationService>()

        operator fun invoke() : AuthenticationService {
            return instance.get() ?: throw AuthExceptions.AuthenticationServiceNotInitialized
        }
    }
}