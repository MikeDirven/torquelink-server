package nl.torquelink.shared.filters

import nl.torquelink.shared.filters.dto.Filter
import nl.torquelink.shared.filters.enums.FilterOperators
import nl.torquelink.shared.filters.exceptions.FilterApiExceptions
import nl.torquelink.shared.filters.interfaces.FiltersBuilder


class Filters(
    block: (Filters.() -> Unit)? = null
) : FiltersBuilder {
    override var filters: List<Filter> = listOf()

    init {
        block?.let { it(this) }
    }

    fun forEach(action: (Filter) -> Unit) {
        filters.forEach(action)
    }

    fun <R: Any> map(transform: (Filter) -> R) : List<R> {
        return filters.map(transform)
    }

    override fun build(): String = StringBuilder().apply {
        filters.forEachIndexed { index, filter ->
            append("${filter.fieldName}|${filter.filterOperator}|${filter.filterValue}")
            if (index != filters.lastIndex) append("+")
        }
    }.toString()

    companion object {
        operator fun invoke(filterString: String): Filters = try {
            Filters {
                addFilters(
                    filterString.split("+").map {
                        it.split("|").let {
                            Filter(
                                it[0],
                                FilterOperators.valueOf(it[1]),
                                it[2]
                            )
                        }
                    }
                )
            }
        } catch (e: Exception) {
            throw FilterApiExceptions.UnableToParseParameters(filterString)
        }

        fun builder(): FiltersBuilder = Filters()
    }
}