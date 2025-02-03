package nl.torquelink.shared.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import nl.torquelink.shared.filters.Filters

object FiltersSerializer : KSerializer<Filters> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Filters"){
        element<String>("filters")
    }

    override fun deserialize(decoder: Decoder): Filters {
        val decodedString = decoder.decodeString()

        requireNotNull(decodedString) { "Missing or invalid 'filters' parameter" }

        return Filters(decodedString)
    }

    override fun serialize(encoder: Encoder, value: Filters) {
        encoder.encodeString(value.build())
    }
}