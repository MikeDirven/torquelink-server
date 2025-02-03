package nl.torquelink.config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import nl.torquelink.shared.filters.Filters
import nl.torquelink.shared.serializers.FiltersSerializer

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(ContentNegotiation){
        json(
            Json {
                serializersModule = SerializersModule {
                    contextual(Filters::class, FiltersSerializer)
                }
            }
        )
    }
}