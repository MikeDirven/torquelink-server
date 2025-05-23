package nl.torquelink.routing.users.exception

sealed class UserApiExceptions(override val message: String? = null) : Exception() {
    object UserProfileNotFoundException : UserApiExceptions(
        "The user profile with was not found"
    )

    class UserProfileIdNotFoundException(val id: Long) : UserApiExceptions(
        "The user profile with id '$id' was not found"
    )

    object NoProfileAvatarInRequest : UserApiExceptions(
        "No profile avatar was provided in the request"
    )
}