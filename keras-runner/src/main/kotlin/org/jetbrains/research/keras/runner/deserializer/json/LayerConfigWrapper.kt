package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.*

enum class LayerType {
    DENSE,
    DROPOUT
}

@Serializable
sealed class LayerConfigWrapper {
    abstract val type: LayerType
    abstract val config: LayerConfig

    @Serializable
    @SerialName("Dense")
    data class DenseLayerConfigWrapper(
        @Transient override val type: LayerType = LayerType.DENSE,
        override val config: LayerConfig.Dense
    ): LayerConfigWrapper()

    @Serializable
    @SerialName("Dropout")
    data class DropoutLayerConfigWrapper(
        @Transient override val type: LayerType = LayerType.DROPOUT,
        override val config: LayerConfig.Dropout
    ): LayerConfigWrapper()
}
