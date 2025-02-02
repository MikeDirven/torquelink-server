package nl.torquelink.opendata.models

import io.ktor.http.*

interface OpenDataResult<T> {
    val status: HttpStatusCode

    data class OpenDataSuccessResult<T>(
        override val status: HttpStatusCode,
        val data: T
    ) : OpenDataResult<T>

    data class OpenDataFailureResult<T>(
        override val status: HttpStatusCode,
        val error: String
    ) : OpenDataResult<T>
}