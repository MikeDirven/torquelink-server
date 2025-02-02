package nl.torquelink.services

import io.ktor.server.application.*
import nl.torquelink.database.dao.AccessTokenStoreDao
import nl.torquelink.database.dao.IdentityDao
import nl.torquelink.database.dao.RememberTokenStoreDao
import nl.torquelink.database.interfaces.DatabaseHolder
import nl.torquelink.database.tables.AccessTokenStoreTable
import nl.torquelink.database.tables.IdentityTable
import nl.torquelink.database.tables.RememberTokenStoreTable
import nl.torquelink.exception.AuthExceptions
import nl.torquelink.interfaces.TokenGenerator
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.auth.RegistrationRequests
import org.jetbrains.exposed.sql.or
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicReference

class AuthenticationService internal constructor (
    private val application: Application,
    private val database: DatabaseHolder,
    private val tokenGenerator: TokenGenerator
) {
    init {
        instance.set(this)
    }
    private suspend fun RegistrationRequests.RegisterWithTorqueLinkDto.registerNewIdentityFromTorqueLink() : IdentityDao{
        return try {
            IdentityDao.new {
                username = this@registerNewIdentityFromTorqueLink.username
                email = this@registerNewIdentityFromTorqueLink.email
                passwordHash = this@registerNewIdentityFromTorqueLink.password
                rememberTokens = emptyList()
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

            rememberToken.identity.handleLoadedAuthenticationRequest(
                rememberToken.identity.passwordHash
            )

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

    companion object {
        val instance = AtomicReference<AuthenticationService>()

        operator fun invoke() : AuthenticationService {
            return instance.get() ?: throw AuthExceptions.AuthenticationServiceNotInitialized
        }
    }
}