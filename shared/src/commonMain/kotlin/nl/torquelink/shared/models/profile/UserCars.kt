package nl.torquelink.shared.models.profile

import kotlinx.serialization.Serializable

@Serializable
sealed interface UserCars {
    val licensePlate: String?
    val vehicleType: String?
    val brand: String?
    val tradeName: String?
    val primaryColor: String?
    val secondaryColor: String?
    val driveReadyMassWeight: Int?
    val variant: String?
    val model: String?

    @Serializable
    data class UserCarWithoutEngineDetailsDto(
        val id: Long,
        override val licensePlate: String,
        override val vehicleType: String,
        override val brand: String,
        override val tradeName: String,
        override val primaryColor: String,
        override val secondaryColor: String,
        override val driveReadyMassWeight: Int,
        override val variant: String,
        override val model: String,
        val photos: List<UserCarsPhotos.UserCarPhotoDto>
    ) : UserCars

    @Serializable
    data class UserCarWithEngineDetailsDto(
        val id: Long,
        override val licensePlate: String,
        override val vehicleType: String,
        override val brand: String,
        override val tradeName: String,
        override val primaryColor: String,
        override val secondaryColor: String,
        override val driveReadyMassWeight: Int,
        override val variant: String,
        override val model: String,
        val numberOfCylinders: Int,
        val cylinderCapacity: Int,
        val readyMassPower: Int,
        val photos: List<UserCarsPhotos.UserCarPhotoDto>
    ) : UserCars

    @Serializable
    data class UserCarCreateDto(
        override val licensePlate: String,
        override val vehicleType: String,
        override val brand: String,
        override val tradeName: String,
        override val primaryColor: String,
        override val secondaryColor: String,
        override val driveReadyMassWeight: Int,
        override val variant: String,
        override val model: String,
        val numberOfCylinders: Int,
        val cylinderCapacity: Int,
        val readyMassPower: Int
    ) : UserCars
}