package nl.torquelink.shared.filters.exposed

import nl.torquelink.shared.filters.Filters
import nl.torquelink.shared.filters.dto.Filter
import nl.torquelink.shared.filters.enums.FilterOperators
import nl.torquelink.shared.filters.exceptions.FilterApiExceptions
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.ops.InListOrNotInListBaseOp
import org.jetbrains.exposed.sql.ops.SingleValueInListOp
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties


inline fun <reified T: Table> SqlExpressionBuilder.sqlFilterExpressionBuilder(table: T, filters: Filters) : Op<Boolean> {
    return SqlFilterExpressionBuilder(T::class, table, filters, null).expression
        ?: throw FilterApiExceptions.UnableToCreateExpressionsFromFilters(filters.filters)
}

inline fun <reified T: Table> Op<Boolean>.sqlFilterExpressionBuilder(table: T, filters: Filters) : Op<Boolean> {
    return SqlFilterExpressionBuilder(T::class, table, filters, this).expression
        ?: throw FilterApiExceptions.UnableToCreateExpressionsFromFilters(filters.filters)
}

inline fun <reified T: Table> T.createSqlExpression(filters: Filters?) : (SqlExpressionBuilder.() -> Op<Boolean>)?
    = filters?.let {
        {
            sqlFilterExpressionBuilder(this@createSqlExpression, filters)
        }
    }

class SqlFilterExpressionBuilder<T: Table>(
    private val klass: KClass<T>,
    private val table: T,
    filters: Filters,
    var expression: Op<Boolean>? = null
){
    init {
        filters.forEach { filter ->
            appendFilter(filter)
        }
    }

    private fun Filter.getTableColumn() : Column<*> = (klass.declaredMemberProperties.firstOrNull {
        it.name == this.fieldName
    } as? KProperty1<Table, Column<*>>)?.get(table) ?: throw FilterApiExceptions.UnableToGetTableColumn(this.fieldName, table.tableName)

    private fun appendFilter(filter: Filter) {
        expression = expression?.let {
            it and (
                    filter.getTableColumn().let { column ->
                        filter.filterOperator.getExpression(
                            column,
                            filter
                        )
                    }
            )
        } ?: filter.getTableColumn().let { column ->
            filter.filterOperator.getExpression(
                column,
                filter
            )
        }
    }

    private fun FilterOperators.getExpression(property: Column<*>, filter: Filter) : Op<Boolean> = when(this){
        FilterOperators.IS_EQUAL -> property eq Filter.parseValueType(filter.filterValue)
        FilterOperators.IS_NOT_EQUAL -> property neq Filter.parseValueType(filter.filterValue)
        FilterOperators.IS_GREATER_THEN -> property greater Filter.parseValueType(filter.filterValue)
        FilterOperators.IS_GREATER_THEN_OR_EQUAL -> property greaterEq  Filter.parseValueType(filter.filterValue)
        FilterOperators.IS_LESS_THEN -> property less  Filter.parseValueType(filter.filterValue)
        FilterOperators.IS_LESS_THEN_OR_EQUAL -> property lessEq Filter.parseValueType(filter.filterValue)
        FilterOperators.IN -> property inList Filter.parseListValues(filter.filterValue)
        FilterOperators.NOT_IN -> property notInList Filter.parseListValues(filter.filterValue)
        FilterOperators.CONTAINS -> property eq Filter.parseValueType(filter.filterValue)
    }

    // Comparators
    private infix fun ExpressionWithColumnType<*>.eq(t: Any): Op<Boolean> = EqOp(this, wrap(t))
    private infix fun ExpressionWithColumnType<*>.neq(t: Any): Op<Boolean> = NeqOp(this, wrap(t))
    private infix fun ExpressionWithColumnType<*>.greater(t: Any): Op<Boolean> = GreaterOp(this, wrap(t))
    private infix fun ExpressionWithColumnType<*>.greaterEq(t: Any): Op<Boolean> = GreaterEqOp(this, wrap(t))
    private infix fun ExpressionWithColumnType<*>.less(t: Any): Op<Boolean> = LessOp(this, wrap(t))
    private infix fun ExpressionWithColumnType<*>.lessEq(t: Any): Op<Boolean> = LessEqOp(this, wrap(t))
    private infix fun ExpressionWithColumnType<*>.inList(list: Iterable<*>): InListOrNotInListBaseOp<*> = SingleValueInListOp(this, list, isInList = true)
    private infix fun ExpressionWithColumnType<*>.notInList(list: Iterable<*>): InListOrNotInListBaseOp<*> = SingleValueInListOp(this, list, isInList = false)

    // Wrappers
    private fun ExpressionWithColumnType<*>.wrap(value: Any): QueryParameter<*> = when (value) {
        is Boolean -> booleanParam(value)
        is Byte -> byteParam(value)
        is UByte -> ubyteParam(value)
        is Short -> shortParam(value)
        is UShort -> ushortParam(value)
        is Int -> intParam(value)
        is UInt -> uintParam(value)
        is Long -> longParam(value)
        is ULong -> ulongParam(value)
        is Float -> floatParam(value)
        is Double -> doubleParam(value)
        is String -> QueryParameter(value, columnType as IColumnType<Any>) // String value should inherit from column
        else -> QueryParameter(value, columnType as IColumnType<Any>)
    } as QueryParameter<*>
}