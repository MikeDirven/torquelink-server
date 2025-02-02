package nl.torquelink.opendata

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import nl.torquelink.opendata.services.VehicleService

object OpenDataApi {
    private val client by lazy {
        HttpClient {
            install(ContentNegotiation){
                json()
            }
        }
    }

    val vehicleData by lazy {
        VehicleService(client)
    }
}