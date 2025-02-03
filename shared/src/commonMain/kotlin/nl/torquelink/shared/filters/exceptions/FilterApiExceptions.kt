package nl.torquelink.shared.filters.exceptions

import nl.torquelink.shared.filters.dto.Filter

sealed class FilterApiExceptions(msg: String) : Exception(msg) {
    class UnsupportedValueType(type: String) : FilterApiExceptions("value with type: $type is not supported!")
    class EmptyListNotAllowed : FilterApiExceptions("empty list is not allowed!")
    class UnableToParseParameters(parametersString: String) : FilterApiExceptions("unable to parse parameters string: $parametersString")

    // Exposed exceptions
    class UnableToCreateExpressionsFromFilters(filters: List<Filter>) : FilterApiExceptions(
        StringBuilder().apply {
            appendLine("Unable to create expressions for filters:")
            filters.forEachIndexed { index, filter ->
                appendLine("$index -> ${filter.fieldName}  -  ${filter.filterOperator.name}  -  ${filter.filterValue}")
            }
        }.toString()
    )
    class UnableToGetTableColumn(column: String, table: String) : FilterApiExceptions("unable to get column: $column from table: $table")
}