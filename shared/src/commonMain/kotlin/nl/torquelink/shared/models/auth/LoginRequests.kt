package nl.torquelink.shared.models.auth

import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginRequests {

    @Serializable
    data class UsernameLoginRequest(
        val password: String,
        val username: String,
        val remember: Boolean? = null
    ) : LoginRequests

    @Serializable
    data class EmailLoginRequest(
        val password: String,
        val email: String,
        val remember: Boolean? = null
    ) : LoginRequests

    @Serializable
    data class RememberTokenLoginRequest(
        val rememberToken: String
    ) : LoginRequests
}
