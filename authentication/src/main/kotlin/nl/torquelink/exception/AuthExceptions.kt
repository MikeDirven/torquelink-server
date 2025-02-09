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

    object EmailNotVerified : AuthExceptions(
        "The email address is not yet verified, please verify you email before logging in!"
    )

    object UnableToCreateEmailVerification : AuthExceptions(
        "An error occurred while trying to create an email verification link."
    )

    object EmailVerificationTokenNotFound: AuthExceptions(
        "The provided email verification token was not found."
    )

    object EmailVerificationTokenExpired: AuthExceptions(
        "The provided email verification token has expired."
    )
}