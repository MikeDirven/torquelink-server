package nl.torquelink.database.dao.users

import nl.torquelink.database.interfaces.CoreEntity
import nl.torquelink.database.interfaces.CoreEntityClass
import nl.torquelink.database.tables.users.UserCarsPhotoTable
import nl.torquelink.shared.models.profile.UserCarsPhotos
import org.jetbrains.exposed.dao.id.EntityID

class UserCarPhotoDao(id : EntityID<Long>) : CoreEntity(id, UserCarsPhotoTable) {
    companion object : CoreEntityClass<UserCarPhotoDao>(UserCarsPhotoTable)

    val photoUrl by UserCarsPhotoTable.photoUrl
    val sequence by UserCarsPhotoTable.sequence

    fun toResponse() : UserCarsPhotos.UserCarPhotoDto {
        return UserCarsPhotos.UserCarPhotoDto(
            id = id.value,
            photoUrl = photoUrl,
            sequence = sequence
        )
    }
}