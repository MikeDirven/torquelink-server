package nl.torquelink.routing.groups.exceptions

sealed class GroupApiExceptions(override val message: String? = null) : Exception() {
    object GroupNotFoundException : GroupApiExceptions(
        "The user profile with was not found"
    )

    class GroupIdNotFoundException(val id: Long) : GroupApiExceptions(
        "The user profile with id '$id' was not found"
    )
}