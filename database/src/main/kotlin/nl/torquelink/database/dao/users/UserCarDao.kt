package nl.torquelink.database.dao.users

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.users.UserCarsPhotoTable
import nl.torquelink.database.tables.users.UserCarsTable
import nl.torquelink.shared.models.profile.UserCars
import org.jetbrains.exposed.dao.id.EntityID

class UserCarDao(id : EntityID<Long>) : CoreEntity(id, UserCarsTable) {
    companion object : CoreEntityClass<UserCarDao>(UserCarsTable)

    var user by UserCarsTable.user
    var licensePlate by UserCarsTable.licensePlate
    var vehicleType by UserCarsTable.vehicleType
    var brand by UserCarsTable.brand
    var tradeName by UserCarsTable.tradeName
    var primaryColor by UserCarsTable.primaryColor
    var secondaryColor by UserCarsTable.secondaryColor
    var driveReadyMassWeight by UserCarsTable.driveReadyMassWeight
    var variant by UserCarsTable.variant
    var model by UserCarsTable.model

    // Engine details
    var numberOfCylinders by UserCarsTable.numberOfCylinders
    var cylinderCapacity by UserCarsTable.cylinderCapacity
    var readyMassPower by UserCarsTable.readyMassPower

    // photos
    val photos by UserCarPhotoDao referrersOn UserCarsPhotoTable.userCar

    fun toResponseWithoutEngineDetails() : UserCars.UserCarWithoutEngineDetailsDto {
        return UserCars.UserCarWithoutEngineDetailsDto(
            id = id.value,
            licensePlate = licensePlate,
            vehicleType = vehicleType,
            brand = brand,
            tradeName = tradeName,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            driveReadyMassWeight = driveReadyMassWeight,
            variant = variant,
            model = model,
            photos = photos.map { it.toResponse() }
        )
    }

    fun toResponseWithEngineDetails() : UserCars.UserCarWithEngineDetailsDto {
        return UserCars.UserCarWithEngineDetailsDto(
            id = id.value,
            licensePlate = licensePlate,
            vehicleType = vehicleType,
            brand = brand,
            tradeName = tradeName,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            driveReadyMassWeight = driveReadyMassWeight,
            variant = variant,
            model = model,
            numberOfCylinders = numberOfCylinders,
            cylinderCapacity = cylinderCapacity,
            readyMassPower = readyMassPower,
            photos = photos.map { it.toResponse() }
        )
    }
}