package nl.torquelink.database.converter

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.LazySizedCollection
import org.jetbrains.exposed.sql.SizedIterable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

fun Any.instanceOf(type: KClass<*>): Boolean = type.java.isInstance(this)

fun LocalDate.toStringFormat(): String {
    val formatter = DateTimeFormatter.ISO_DATE
    return this.format(formatter)
}

fun LocalDateTime.toUtcString(): String {
    val instant = this.toInstant(ZoneOffset.UTC)
    val zonedDateTime = instant.atZone(ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'") // Predefined formatter for UTC
    return zonedDateTime.format(formatter)
}

inline fun <reified Dao: Entity<*>, reified Response> Dao.toResponse() : Response = this::class.members.let { doaParameters ->
    val response = Response::class
    val responseObjectParameters = Response::class.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.parameters
    response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.call(
        *responseObjectParameters.map { responseParam ->
            doaParameters.find { it.name == responseParam.name }?.let { doaParameter ->
                when{
                    doaParameter.call(this)?.instanceOf(EntityID::class) ?: false -> (doaParameter.call(this) as EntityID<*>).value
                    doaParameter.call(this)?.instanceOf(Entity::class) ?: false -> (doaParameter.call(this) as Entity<*>).handleObject(responseParam.type)
                    doaParameter.call(this)?.instanceOf(LazySizedCollection::class) ?: false -> (doaParameter.call(this) as LazySizedCollection<Entity<*>>).handleList(responseParam.type)
                    doaParameter.call(this)?.instanceOf(SizedIterable::class) ?: false -> (doaParameter.call(this) as SizedIterable<Entity<*>>).handleList(responseParam.type)
                    doaParameter.call(this)?.instanceOf(LocalDateTime::class) ?: false -> (doaParameter.call(this) as LocalDateTime).toUtcString()
                    doaParameter.call(this)?.instanceOf(LocalDate::class) ?: false -> (doaParameter.call(this) as LocalDate).toStringFormat()
                    else -> doaParameter.call(this)
                }
            }
        }.toTypedArray()
    )
}

inline fun <reified Dao: Entity<*>, reified Response> SizedIterable<Dao>.toResponse() : List<Response> = this.map { dao ->
    dao::class.members.let { doaParameters ->
        val response = Response::class
        val responseObjectParameters = Response::class.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.parameters
        response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.call(
            *responseObjectParameters.map { responseParam ->
                doaParameters.find { it.name == responseParam.name }?.let { doaParameter ->
                    when{
                        doaParameter.call(dao)?.instanceOf(EntityID::class) ?: false -> (doaParameter.call(dao) as EntityID<*>).value
                        doaParameter.call(dao)?.instanceOf(LazySizedCollection::class) ?: false -> (doaParameter.call(dao) as LazySizedCollection<Entity<*>>).handleList(responseParam.type)
                        doaParameter.call(dao)?.instanceOf(SizedIterable::class) ?: false -> (doaParameter.call(dao) as SizedIterable<Entity<*>>).handleList(responseParam.type)
                        doaParameter.call(dao)?.instanceOf(Entity::class) ?: false -> (doaParameter.call(dao) as Entity<*>).handleObject(responseParam.type)
                        doaParameter.call(dao)?.instanceOf(LocalDateTime::class) ?: false -> (doaParameter.call(dao) as LocalDateTime).toUtcString()
                        doaParameter.call(dao)?.instanceOf(LocalDate::class) ?: false -> (doaParameter.call(dao) as LocalDate).toStringFormat()
                        else -> try {
                            doaParameter.call(dao)
                        } catch (e: Exception){
                            dao.handleObject(responseParam.type)
                        }
                    }
                }
            }.toTypedArray()
        )
    }
}

fun <Dao: Entity<*>> Dao.handleObject(responseParam: KType) : Any = this::class.members.let { doaParameters ->
    val response = responseParam.jvmErasure
    response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.call(
        *response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.parameters.map { responseParam ->
            doaParameters.find { it.name == responseParam.name }?.let { doaParameter ->
                when{
                    doaParameter.call(this)?.instanceOf(EntityID::class) ?: false -> (doaParameter.call(this) as EntityID<*>).value
                    doaParameter.call(this)?.instanceOf(LazySizedCollection::class) ?: false -> (doaParameter.call(this) as LazySizedCollection<Entity<*>>).handleList(responseParam.type)
                    doaParameter.call(this)?.instanceOf(SizedIterable::class) ?: false -> (doaParameter.call(this) as SizedIterable<Entity<*>>).handleList(responseParam.type)
                    doaParameter.call(this)?.instanceOf(Entity::class) ?: false -> (doaParameter.call(this) as Entity<*>).handleObject(responseParam.type)
                    doaParameter.call(this)?.instanceOf(LocalDateTime::class) ?: false -> (doaParameter.call(this) as LocalDateTime).toUtcString()
                    doaParameter.call(this)?.instanceOf(LocalDate::class) ?: false -> (doaParameter.call(this) as LocalDate).toStringFormat()
                    else -> doaParameter.call(this)
                }
            }
        }.toTypedArray()
    )
}

inline fun <reified Dao: Entity<*>> LazySizedCollection<Dao>.handleList(responseParam: KType): List<*> = this.wrapper.map { daoEntry ->
    val response = responseParam.arguments.first().type!!.jvmErasure
    response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.call(
        *response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.parameters.map { responseParam ->
            daoEntry::class.members.find { it.name == responseParam.name }?.let { doaParameter ->
                val daoValue = doaParameter.call(daoEntry)
                when{
                    daoValue?.instanceOf(EntityID::class) ?: false -> (daoValue as EntityID<*>).value
                    daoValue?.instanceOf(Entity::class) ?: false -> (daoValue as Entity<*>).handleObject(responseParam.type)
                    daoValue?.instanceOf(LazySizedCollection::class) ?: false -> (daoValue as LazySizedCollection<Entity<*>>).innerList(responseParam.type)
                    daoValue?.instanceOf(SizedIterable::class) ?: false -> (daoValue as SizedIterable<Entity<*>>).innerList(responseParam.type)
                    daoValue?.instanceOf(LocalDateTime::class) ?: false -> (daoValue as LocalDateTime).toUtcString()
                    daoValue?.instanceOf(LocalDate::class) ?: false -> (daoValue as LocalDate).toStringFormat()
                    else -> try {
                        daoValue
                    } catch (e: Exception){
                        daoEntry.handleObject(responseParam.type)
                    }
                }
            }
        }.toTypedArray()
    )
}

inline fun <reified Dao: Entity<*>> SizedIterable<Dao>.handleList(responseParam: KType): List<*> = this.map { daoEntry ->
    val response = responseParam.arguments.first().type!!.jvmErasure
    response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.call(
        *response.constructors.first { !it.parameters.any { it.name == "serializationConstructorMarker" } }.parameters.map { responseParam ->
            daoEntry::class.members.find { it.name == responseParam.name }?.let { doaParameter ->
                when{
                    doaParameter.call(daoEntry)?.instanceOf(EntityID::class) ?: false -> (doaParameter.call(daoEntry) as EntityID<*>).value
                    doaParameter.call(daoEntry)?.instanceOf(Entity::class) ?: false-> (doaParameter.call(daoEntry) as Entity<*>).handleObject(responseParam.type)
                    doaParameter.call(daoEntry)?.instanceOf(LazySizedCollection::class) ?: false-> (doaParameter.call(daoEntry) as LazySizedCollection<Entity<*>>).innerList(responseParam.type)
                    doaParameter.call(daoEntry)?.instanceOf(SizedIterable::class) ?: false-> (doaParameter.call(daoEntry) as SizedIterable<Entity<*>>).innerList(responseParam.type)
                    doaParameter.call(daoEntry)?.instanceOf(LocalDateTime::class) ?: false -> (doaParameter.call(daoEntry) as LocalDateTime).toUtcString()
                    doaParameter.call(daoEntry)?.instanceOf(LocalDate::class) ?: false -> (doaParameter.call(daoEntry) as LocalDate).toStringFormat()
                    else -> try {
                        doaParameter.call(daoEntry)
                    } catch (e: Exception){
                        daoEntry.handleObject(responseParam.type)
                    }
                }
            }
        }.toTypedArray()
    )
}

fun LazySizedCollection<Entity<*>>.innerList(responseParam: KType) = this.handleList(responseParam)

fun SizedIterable<Entity<*>>.innerList(responseParam: KType) = this.handleList(responseParam)