package nl.torquelink.routing.groups.exceptions

sealed class GroupApiExceptions(override val message: String? = null) : Exception() {
    class GroupIdNotFoundException(id: Long) : GroupApiExceptions(
        "The user group with id '$id' was not found"
    )

    class GroupNameAlreadyExists(groupName: String) : GroupApiExceptions(
        "The group name '$groupName' is already in use"
    )

    class NoAdminRightForGroup(group: String) : GroupApiExceptions(
        "No admin right granted for group: $group"
    )
}