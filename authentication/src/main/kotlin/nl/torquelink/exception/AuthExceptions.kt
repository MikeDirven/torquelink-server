package nl.torquelink.exception

import java.time.LocalDateTime

sealed class AuthExceptions(override val message: String? = null) : Exception() {
    object AuthenticationServiceNotInitialized : AuthExceptions(
        "The authentication service has not been initialized."
    )

    object NoValidTokenFound : AuthExceptions(
        "No valid access or refresh token was found in the request."
    )

    object AccessTokenInvalided : AuthExceptions(
        "The provided access token is invalid or expired."
    )

    object RefreshTokenInvalided : AuthExceptions(
        "The provided refresh token is invalid or expired."
    )

    object RememberTokenInvalided : AuthExceptions(
        "The provided remember token is invalid."
    )

    class UserNotFoundWithUsername(val username: String) : AuthExceptions(
        "No user found with the username '$username'."
    )

    class UserNotFoundWithEmail(val email: String) : AuthExceptions(
        "No user found with the email '$email'."
    )

    object InvalidCredentials : AuthExceptions("Invalid credentials provided.")

    class AuthLockedOut(val lockedUntil: LocalDateTime) : AuthExceptions(
        "The account is locked out until $lockedUntil."
    )
}