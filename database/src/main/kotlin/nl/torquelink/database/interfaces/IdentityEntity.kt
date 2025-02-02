package nl.torquelink.database.interfaces

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

abstract class IdentityEntity(id: EntityID<UUID>, table : IdentityTable) : UUIDEntity(id) {
    var active by table.active
    val creationDate by table.creationDate
    var mutationDate by table.mutationDate
}