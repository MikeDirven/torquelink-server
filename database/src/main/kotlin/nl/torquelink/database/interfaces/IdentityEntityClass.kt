package nl.torquelink.database.interfaces

import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.toEntity
import java.time.LocalDateTime

open class IdentityEntityClass<E: IdentityEntity>(table: IdentityTable) : UUIDEntityClass<E>(table) {
    init {
        EntityHook.subscribe { action ->
            when(action.changeType){
                EntityChangeType.Updated -> action.toEntity(this)?.mutationDate = LocalDateTime.now()
                else -> Unit
            }
        }
    }
}