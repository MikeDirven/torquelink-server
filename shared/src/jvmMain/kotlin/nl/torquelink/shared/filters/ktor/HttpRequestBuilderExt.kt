package nl.torquelink.shared.filters.ktor

import io.ktor.client.request.*
import nl.torquelink.shared.filters.Filters

fun HttpRequestBuilder.filters(configure: Filters.() -> Unit) {
    parameter("filters", Filters(configure).build())
}

fun HttpRequestBuilder.filters(filters: Filters) {
    parameter("filters", filters.build())
}