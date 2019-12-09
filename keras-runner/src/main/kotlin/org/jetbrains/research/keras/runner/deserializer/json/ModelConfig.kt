package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class ModelConfig {
    abstract val name: String
    abstract val layers: List<LayerConfigWrapper>

    @Serializable
    data class Sequential(
        override val name: String,
        override val layers: List<LayerConfigWrapper>
    ) : ModelConfig() {
        @Transient
        val batchInputShape = layers.first().config.batchInputShape!!.filterNotNull()
    }
}
