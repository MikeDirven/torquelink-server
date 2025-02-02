package nl.torquelink.opendata.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import nl.torquelink.opendata.models.OpenDataResult
import nl.torquelink.opendata.models.OpenDataVehicle

class VehicleService(
    private val client: HttpClient
) {
    private suspend inline fun <reified T> HttpResponse.parseResult(): OpenDataResult<T> {
        return when(status) {
            HttpStatusCode.OK -> {
                val deSerialized = body<T>()
                OpenDataResult.OpenDataSuccessResult(status, deSerialized)
            }
            else -> OpenDataResult.OpenDataFailureResult(status, bodyAsText())
        }
    }

    suspend fun getVehicleData(registrationNumber: String): OpenDataResult<List<OpenDataVehicle>> {
        return client.get(
            "https://opendata.rdw.nl/resource/m9d7-ebf2.json?kenteken=${registrationNumber.uppercase()}"
        ){
            header("X-App-Token", "dqQfV1Q4UkFFn7vVeuZjb1Qv2")
        }.parseResult()
    }
}