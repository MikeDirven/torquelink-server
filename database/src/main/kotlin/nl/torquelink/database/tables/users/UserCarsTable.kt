package nl.torquelink.database.tables.users

import nl.torquelink.database.interfaces.CoreTable

object UserCarsTable : CoreTable("TL_D_User_Cars") {
    override val active = bool("active").default(true)
    val user = reference("user", UserProfileTable)

    // Body details
    val licensePlate = varchar("licensePlate", 50)
    val vehicleType = varchar("vehicleType", 100)
    val brand = varchar("brand", 100)
    val tradeName = varchar("tradeName", 150)
    val primaryColor = varchar("primaryColor", 255)
    val secondaryColor = varchar("secondaryColor", 255)
    val driveReadyMassWeight = integer("driveReadyMassWeight")
    val variant = varchar("variant", 255)
    val model = varchar("model", 255)

    // Engine details
    val numberOfCylinders = integer("numberOfCylinders")
    val cylinderCapacity = integer("cylinderCapacity")
    val readyMassPower = integer("readyMassPower")
}