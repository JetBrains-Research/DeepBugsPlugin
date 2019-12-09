package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.*

@Serializable
enum class ActivationType {
    @SerialName("relu") ReLU,
    @SerialName("sigmoid") SIGMOID
}
