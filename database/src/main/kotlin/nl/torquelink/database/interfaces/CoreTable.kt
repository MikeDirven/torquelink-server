package nl.torquelink.database.interfaces

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

abstract class CoreTable(tableName: String) : LongIdTable(tableName) {
    abstract val active : Column<Boolean>

    val creationDate = datetime("creation_date").defaultExpression(CurrentDateTime)
    val mutationDate = datetime("mutation_date").defaultExpression(CurrentDateTime)
}