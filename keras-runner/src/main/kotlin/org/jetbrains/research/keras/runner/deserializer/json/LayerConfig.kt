package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class LayerConfig {
    abstract val name: String
    abstract val trainable: Boolean
    abstract val batchInputShape: List<Int?>?
    abstract val dtype: String?

    @Serializable
    data class Dense(
        override val name: String,
        override val trainable: Boolean,
        @SerialName("batch_input_shape") override var batchInputShape: List<Int?>? = null,
        override val dtype: String? = null,
        var units: Int,
        var activation: ActivationType,
        @SerialName("use_bias") var useBias: Boolean
    ) : LayerConfig()

    @Serializable
    data class Dropout(
        override val name: String,
        override val trainable: Boolean,
        @SerialName("batch_input_shape") override val batchInputShape: List<Int?>? = null,
        override val dtype: String? = null,
        val rate: Double
    ) : LayerConfig()
}
