package nl.torquelink.shared.models.profile

import kotlinx.serialization.Serializable

@Serializable
sealed interface UserCarsPhotos {
    val photoUrl: String
    val sequence: Long

    @Serializable
    data class UserCarPhotoDto(
        val id: Long,
        override val photoUrl: String,
        override val sequence: Long
    ) : UserCarsPhotos
}