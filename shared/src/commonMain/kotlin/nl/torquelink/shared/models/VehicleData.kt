package nl.torquelink.shared.models

interface VehicleData {
    val licensePlate: String
    val vehicleType: String
    val bodyType: String
    val brand: String
    val model: String
    val color: String
    val horsePower: Int
    val lookUpAmount: Int

    data class VehicleDataDto(
        override val licensePlate: String,
        override val vehicleType: String,
        override val bodyType: String,
        override val color: String,
        override val brand: String,
        override val model: String,
        override val horsePower: Int,
        override val lookUpAmount: Int,
    ) : VehicleData

    data class VehicleDataDetailedDto(
        override val licensePlate: String,
        override val vehicleType: String,
        override val bodyType: String,
        override val brand: String,
        override val model: String,
        override val color: String,
        override val horsePower: Int,
        override val lookUpAmount: Int,
        val powerInKw: Int,
        val cylinderCount: Int,
        val displacement: Int,
        val weight: Int,
        val lastAdmissionDate: String,
        val dateFirstAdmission: String
    ) : VehicleData
}