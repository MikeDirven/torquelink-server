package nl.torquelink.generators

import nl.torquelink.interfaces.TokenGenerator
import java.security.SecureRandom
import java.util.*

object TorqueLinkTokenGenerator : TokenGenerator() {
    override fun generateAccessToken(username: String): String {
        val secureRandom = SecureRandom()
        val tokenBytes = ByteArray(username.length + 100)
        secureRandom.nextBytes(tokenBytes);
        val accessToken: String = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        return accessToken
    }

    override fun generateRefreshToken(username: String): String {
        return generateAccessToken(username)
    }

    override fun generateRememberToken(username: String): String {
        return generateAccessToken(username)
    }

    override fun generateVerificationToken(username: String): String {
        return generateAccessToken(username)
    }
}