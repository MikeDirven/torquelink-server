package nl.torquelink.database.interfaces

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

abstract class IdentityTable(tableName: String) : UUIDTable(tableName) {
    abstract val active : Column<Boolean>

    val creationDate = datetime("creation_date").defaultExpression(CurrentDateTime)
    val mutationDate = datetime("mutation_date").defaultExpression(CurrentDateTime)
}