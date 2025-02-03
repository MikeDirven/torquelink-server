package nl.torquelink.shared.filters.ktor

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.ktor.server.routing.*
import nl.torquelink.shared.filters.Filters

fun RoutingContext.filters() : Filters? {
    return call.parameters["filters"]?.let {
        Filters(it)
    }
}

fun OpenApiRoute.addFilters(){
    request {
        queryParameter<String>("filters")
    }
}