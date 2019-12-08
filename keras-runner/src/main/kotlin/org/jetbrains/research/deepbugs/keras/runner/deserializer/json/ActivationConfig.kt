package org.jetbrains.research.deepbugs.keras.runner.deserializer.json

import kotlinx.serialization.*

@Serializable
sealed class ActivationConfig {
    abstract var activation: String

    data class ReLU(override var activation: String) : ActivationConfig()
    data class Sigmoid(override var activation: String) : ActivationConfig()

    @Serializer(forClass = ActivationConfig::class)
    companion object : KSerializer<ActivationConfig> {
        override fun serialize(encoder: Encoder, obj: ActivationConfig) = Unit

        override fun deserialize(decoder: Decoder): ActivationConfig = when (val func = decoder.decodeString()) {
            "relu" -> ReLU(func)
            "sigmoid" -> Sigmoid(func)
            else -> throw IllegalStateException("Unsupported function")
        }
    }
}
