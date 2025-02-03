package nl.torquelink.database.dao.users

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.users.UserCarsTable
import org.jetbrains.exposed.dao.id.EntityID

class UserCarDao(id : EntityID<Long>) : CoreEntity(id, UserCarsTable) {
    companion object : CoreEntityClass<UserCarDao>(UserCarsTable)

    val user by UserProfileDao referencedOn UserCarsTable.user

    val licensePlate by UserCarsTable.licensePlate
    val vehicleType by UserCarsTable.vehicleType
    val brand by UserCarsTable.brand
    val tradeName by UserCarsTable.tradeName
    val primaryColor by UserCarsTable.primaryColor
    val secondaryColor by UserCarsTable.secondaryColor
    val driveReadyMassWeight by UserCarsTable.driveReadyMassWeight
    val variant by UserCarsTable.variant
    val model by UserCarsTable.model

    // Engine details
    val numberOfCylinders by UserCarsTable.numberOfCylinders
    val cylinderCapacity by UserCarsTable.cylinderCapacity
    val readyMassPower by UserCarsTable.readyMassPower
}