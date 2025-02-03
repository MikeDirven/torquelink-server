package nl.torquelink.shared.filters.interfaces

import nl.torquelink.shared.filters.dto.Filter
import nl.torquelink.shared.filters.enums.FilterOperators

interface FiltersBuilder {
    var filters: List<Filter>

    fun build() : String

    fun addFilter(
        fieldName: String,
        filterOperator: FilterOperators,
        filterValue: Any
    ) : FiltersBuilder{
        filters = buildList {
            addAll(filters)
            add(
                Filter(
                    fieldName,
                    filterOperator,
                    filterValue
                )
            )
        }
        return this
    }

    fun addFilter(filter: Filter) : FiltersBuilder {
        filters = buildList {
            addAll(filters)
            add(filter)
        }
        return this
    }

    fun addFilters(filterList: List<Filter>) : FiltersBuilder {
        filters = buildList {
            addAll(filters)
            addAll(filterList)
        }
        return this
    }

    fun addFilter(filter: Filter.() -> Unit) : FiltersBuilder {
        filters = buildList {
            addAll(filters)
            add(Filter().apply(filter))
        }
        return this
    }
}