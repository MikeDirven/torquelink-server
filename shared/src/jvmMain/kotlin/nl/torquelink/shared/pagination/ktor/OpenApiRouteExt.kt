package nl.torquelink.shared.pagination.ktor

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute

fun OpenApiRoute.addPagination() {
    request {
        queryParameter<Int>("page")
        queryParameter<Int>("limit")
    }
}