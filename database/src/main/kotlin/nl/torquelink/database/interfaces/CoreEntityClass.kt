package nl.torquelink.database.interfaces

import org.jetbrains.exposed.dao.EntityChangeType
import org.jetbrains.exposed.dao.EntityHook
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.toEntity
import java.time.LocalDateTime

open class CoreEntityClass<E: CoreEntity>(table: CoreTable) : LongEntityClass<E>(table) {
    init {
        EntityHook.subscribe { action ->
            when(action.changeType){
                EntityChangeType.Updated -> action.toEntity(this)?.mutationDate = LocalDateTime.now()
                else -> Unit
            }
        }
    }
}