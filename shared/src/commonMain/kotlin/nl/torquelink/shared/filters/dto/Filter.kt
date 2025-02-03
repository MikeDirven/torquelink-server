package nl.torquelink.shared.filters.dto

import kotlinx.serialization.Serializable
import nl.torquelink.shared.filters.enums.FilterOperators
import nl.torquelink.shared.filters.exceptions.FilterApiExceptions

@Serializable
data class Filter(
    var fieldName: String,
    var filterOperator: FilterOperators,
    var filterValue: String
) {
    constructor() : this("", FilterOperators.IS_EQUAL, "")

    constructor(
        fieldName: String,
        filterOperator: FilterOperators,
        filterValue: Any
    ) : this(fieldName, filterOperator, getValueType(filterValue))

    companion object {
        fun getValueType(value: Any): String = when (value) {
            is String -> value
            is Int -> "i#$value"
            is Short -> "s#$value"
            is Double -> "d#$value"
            is Boolean -> "b#$value"
            is Long -> "l#$value"
            is Collection<*> -> "c#${
                value.joinToString(","){
                    getValueType(
                        it ?: throw FilterApiExceptions.EmptyListNotAllowed()
                    )
                }
            }"
            else -> throw FilterApiExceptions.UnsupportedValueType(value::class.simpleName ?: "UNKNOWN")
        }

        fun parseValueType(value: String): Comparable<*> = when {
            value.startsWith("i#") -> value.removePrefix("i#").toInt()
            value.startsWith("s#") -> value.removePrefix("s#").toShort()
            value.startsWith("d#") -> value.removePrefix("d#").toDouble()
            value.startsWith("b#") -> value.removePrefix("b#").toBoolean()
            value.startsWith("l#") -> value.removePrefix("l#").toLong()
            else -> value
        }

        fun parseListValues(value: String) : Iterable<*> = value.split(",").map {
            when {
                value.startsWith("i#") -> value.removePrefix("i#").toInt()
                value.startsWith("s#") -> value.removePrefix("s#").toShort()
                value.startsWith("d#") -> value.removePrefix("d#").toDouble()
                value.startsWith("b#") -> value.removePrefix("b#").toBoolean()
                value.startsWith("l#") -> value.removePrefix("l#").toLong()
                else -> this
            }
        }
    }
}
