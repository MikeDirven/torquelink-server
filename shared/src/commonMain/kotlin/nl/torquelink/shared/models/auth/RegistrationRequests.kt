package nl.torquelink.shared.models.auth

import kotlinx.serialization.Serializable

@Serializable
sealed interface RegistrationRequests {
    val username: String
    val email: String
    val password: String

    @Serializable
    data class RegisterWithTorqueLinkDto(
        override val username: String,
        override val email: String,
        override val password: String
    ) : RegistrationRequests
}