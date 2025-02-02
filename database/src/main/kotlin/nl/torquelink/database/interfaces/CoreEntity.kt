package nl.torquelink.database.interfaces

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID

abstract class CoreEntity(id: EntityID<Long>, table : CoreTable) : LongEntity(id) {
    var active by table.active
    val creationDate by table.creationDate
    var mutationDate by table.mutationDate
}