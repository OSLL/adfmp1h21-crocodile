package com.firsttimeinforever.crocodile.model

import android.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeLong(value.pack())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color.valueOf(decoder.decodeLong())
    }
}
