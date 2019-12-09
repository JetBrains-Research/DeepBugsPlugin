package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class LayerConfig {
    abstract var name: String
    abstract var trainable: Boolean
    abstract var batchInputShape: List<Int?>?
    abstract var dtype: String?

    @Serializable
    data class Dense(
        override var name: String,
        override var trainable: Boolean,
        @SerialName("batch_input_shape") override var batchInputShape: List<Int?>? = null,
        override var dtype: String? = null,
        var units: Int,
        var activation: ActivationType,
        @SerialName("use_bias") var useBias: Boolean
    ) : LayerConfig()

    @Serializable
    data class Dropout(
        override var name: String,
        override var trainable: Boolean,
        @SerialName("batch_input_shape") override var batchInputShape: List<Int?>? = null,
        override var dtype: String? = null,
        var rate: Double
    ) : LayerConfig()
}
