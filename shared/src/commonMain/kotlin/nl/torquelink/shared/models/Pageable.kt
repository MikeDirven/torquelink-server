package nl.torquelink.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class Pageable<D>(
    val currentPage: Int,
    val totalPages: Int,
    val limit: Int,
    val totalRecords: Long,
    val data: List<D>,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
)