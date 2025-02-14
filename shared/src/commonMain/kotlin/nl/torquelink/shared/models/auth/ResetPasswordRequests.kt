package nl.torquelink.shared.models.auth

import kotlinx.serialization.Serializable

@Serializable
sealed interface ResetPasswordRequests {
    @Serializable
    data class RequestPasswordReset(
        val username: String,
        val email: String
    ) : ResetPasswordRequests

    @Serializable
    data class ResetPasswordWithTokenToken(
        val token: String,
        val newPassword: String
    ) : ResetPasswordRequests
}