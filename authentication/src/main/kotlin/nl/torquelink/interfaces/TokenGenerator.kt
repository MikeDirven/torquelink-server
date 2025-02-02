package nl.torquelink.interfaces

abstract class TokenGenerator {
    abstract fun generateAccessToken(username: String): String

    abstract fun generateRefreshToken(username: String) : String

    abstract fun generateRememberToken(username: String) : String
}