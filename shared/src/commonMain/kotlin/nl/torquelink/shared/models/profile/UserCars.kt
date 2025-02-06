package nl.torquelink.shared.models.profile

import kotlinx.serialization.Serializable

@Serializable
sealed interface UserCars {
    val id: Long
    val licensePlate: String
    val vehicleType: String
    val brand: String
    val tradeName: String
    val primaryColor: String
    val secondaryColor: String
    val driveReadyMassWeight: Int
    val variant: String
    val model: String

    @Serializable
    data class UserCarWithoutEngineDetailsDto(
        override val id: Long,
        override val licensePlate: String,
        override val vehicleType: String,
        override val brand: String,
        override val tradeName: String,
        override val primaryColor: String,
        override val secondaryColor: String,
        override val driveReadyMassWeight: Int,
        override val variant: String,
        override val model: String
    ) : UserCars
}