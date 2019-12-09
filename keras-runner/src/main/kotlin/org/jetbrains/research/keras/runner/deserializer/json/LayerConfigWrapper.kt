package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    @Suppress("UNUSED")
    data class DenseLayerConfigWrapper(
        @Transient override val type: LayerType = LayerType.DENSE,
        override val config: LayerConfig.Dense
    ) : LayerConfigWrapper()

    @Serializable
    @SerialName("Dropout")
    @Suppress("UNUSED")
    data class DropoutLayerConfigWrapper(
        @Transient override val type: LayerType = LayerType.DROPOUT,
        override val config: LayerConfig.Dropout
    ) : LayerConfigWrapper()
}
