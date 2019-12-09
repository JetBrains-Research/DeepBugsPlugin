package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class LayerConfigWrapper {
    abstract val type: Type
    abstract val config: LayerConfig

    @Serializable
    @SerialName("Dense")
    @Suppress("UNUSED")
    data class DenseLayerConfigWrapper(
        @Transient override val type: Type = Type.DENSE,
        override val config: LayerConfig.Dense
    ) : LayerConfigWrapper()

    @Serializable
    @SerialName("Dropout")
    @Suppress("UNUSED")
    data class DropoutLayerConfigWrapper(
        @Transient override val type: Type = Type.DROPOUT,
        override val config: LayerConfig.Dropout
    ) : LayerConfigWrapper()

    enum class Type {
        DENSE,
        DROPOUT
    }
}
