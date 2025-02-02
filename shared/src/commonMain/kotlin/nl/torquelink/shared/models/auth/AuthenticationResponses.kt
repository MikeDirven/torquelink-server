package nl.torquelink.shared.models.auth

import kotlinx.serialization.Serializable
import nl.torquelink.shared.models.profile.UserProfiles

@Serializable
sealed interface AuthenticationResponses {
    val accessToken: String
    val refreshToken: String

    @Serializable
    data class AuthenticationResponseDefault(
        override val accessToken: String,
        override val refreshToken: String
    ) : AuthenticationResponses

    @Serializable
    data class AuthenticationResponseWithRemember(
        override val accessToken: String,
        override val refreshToken: String,
        val rememberToken: String
    ) : AuthenticationResponses

    @Serializable
    data class AuthenticationResponseWithProfile(
        override val accessToken: String,
        override val refreshToken: String,
        val profile: UserProfiles.UserProfileWithSettingsDto
    ) : AuthenticationResponses

    @Serializable
    data class AuthenticationResponseWithRememberAndProfile(
        override val accessToken: String,
        override val refreshToken: String,
        val rememberToken: String,
        val profile: UserProfiles.UserProfileWithSettingsDto
    ) : AuthenticationResponses
}
