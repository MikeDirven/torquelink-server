package nl.torquelink.database.tables.users

import nl.torquelink.database.interfaces.CoreTable
import kotlin.random.Random

object UserCarsPhotoTable : CoreTable("TL_D_User_Cars") {
    override val active = bool("active").default(true)
    val userCar = reference("userCar", UserCarsTable)

    val photoUrl = largeText("photoUrl")
    val sequence = long("sequence").default(Random.nextLong())
}