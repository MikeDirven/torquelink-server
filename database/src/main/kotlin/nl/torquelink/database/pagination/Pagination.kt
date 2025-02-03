package nl.torquelink.database.pagination

import nl.torquelink.database.exceptions.PageableExceptions
import nl.torquelink.shared.models.Pageable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.*

inline fun <reified R: Any, reified D : Entity<*>> EntityClass<*, D>.paginated(
    page: Int? = null,
    size: Int? = null,
    converter: D.() -> R,
    noinline filter: (SqlExpressionBuilder.() -> Op<Boolean>)? = null
) : Pageable<R> {
    return this.let { dao ->
        if(page != null && page < 1) throw PageableExceptions.InvalidPageException(page)
        if(size != null && size < 1) throw PageableExceptions.InvalidSizeException(size)
        val records = filter?.let { dao.find(it) } ?: dao.all()
        val totalRecord = records.count()
        val _page = page ?: 1
        val _size = size ?: 100
        val _totalPages = (totalRecord / _size.toLong()).toInt().let { if(it <= 0) 1 else it }

        Pageable(
            currentPage = _page,
            totalPages = _totalPages,
            limit = _size,
            totalRecords = totalRecord,
            data = records
                .orderBy(this.table.id to SortOrder.DESC)
                .limit(_size).offset(((_page - 1) * _size).toLong())
                .map(converter),
            hasNext = totalRecord > (_page * _size).toLong(),
            hasPrevious = _page > 1
        )
    }
}

inline fun <reified R: Any> Query.paginated(
    page: Int? = null,
    size: Int? = null,
    idField: Expression<*>? = null,
    noinline filter: (SqlExpressionBuilder.() -> Op<Boolean>)? = null,
    noinline converter: ResultRow.() -> R
) : Pageable<R> {
    return this.let { dao ->
        if(page != null && page < 1) throw PageableExceptions.InvalidPageException(page)
        if(size != null && size < 1) throw PageableExceptions.InvalidSizeException(size)
        val records = filter?.let { this.where(it) } ?: this
        val totalRecord = records.count()
        val _page = page ?: 1
        val _size = size ?: 100
        val _totalPages = (totalRecord / _size.toLong()).toInt().let { if(it <= 0) 1 else it }

        Pageable(
            currentPage = _page,
            totalPages = _totalPages,
            limit = _size,
            totalRecords = totalRecord,
            data = records.let { query ->
                idField?.let { query.orderBy(it to SortOrder.DESC) } ?: query
            }
                .limit(_size).offset(((_page - 1) * _size).toLong())
                .map(converter),
            hasNext = totalRecord > (_page * _size).toLong(),
            hasPrevious = _page > 1
        )
    }
}