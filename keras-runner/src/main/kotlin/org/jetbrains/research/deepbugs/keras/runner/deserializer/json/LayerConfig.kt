package org.jetbrains.research.deepbugs.keras.runner.deserializer.json

import kotlinx.serialization.*
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.JsonObject

sealed class LayerConfig {
    abstract var name: String
    abstract var trainable: Boolean
    abstract var batchInputShape: List<Int?>?
    abstract var dtype: String?

    @Serializable
    data class Dense(
        override var name: String,
        override var trainable: Boolean,
        @SerialName("batch_input_shape") override var batchInputShape: List<Int?>?,
        override var dtype: String?,
        var units: Int,
        @Serializable(with = ActivationConfig.Companion::class) var activation: ActivationConfig,
        @SerialName("use_bias") var useBias: Boolean
    ) : LayerConfig() {
        @Serializer(forClass = Dense::class)
        companion object : KSerializer<Dense> {
            override fun serialize(encoder: Encoder, obj: Dense) = Unit

            override fun deserialize(decoder: Decoder): Dense {
                val compositeDecoder = decoder.beginStructure(descriptor)
                var name: String? = null
                var trainable: Boolean? = null
                var batchInputShape: List<Int?>? = null
                var dtype: String? = null
                var units: Int? = null
                var activation: ActivationConfig? = null
                var useBias: Boolean? = null
                mainLoop@ while (true) {
                    when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                        CompositeDecoder.READ_DONE -> {
                            break@mainLoop
                        }
                        0 -> name = compositeDecoder.decodeStringElement(descriptor, index)
                        1 -> trainable = compositeDecoder.decodeBooleanElement(descriptor, index)
                        2 -> batchInputShape = compositeDecoder.decodeSerializableElement(descriptor, index, Int.serializer().nullable.list.nullable)
                        3 -> dtype = compositeDecoder.decodeStringElement(descriptor, index)
                        4 -> units = compositeDecoder.decodeIntElement(descriptor, index)
                        5 -> activation = compositeDecoder.decodeSerializableElement(descriptor, index, ActivationConfig.serializer())
                        6 -> useBias = compositeDecoder.decodeBooleanElement(descriptor, index)
                        else -> compositeDecoder.decodeNullableSerializableElement(Dropout.descriptor, index, JsonObject.serializer().nullable)
                    }
                }
                compositeDecoder.endStructure(descriptor)
                return Dense(name!!, trainable!!, batchInputShape, dtype, units!!, activation!!, useBias!!)
            }
        }
    }

    @Serializable
    data class Dropout(
        override var name: String,
        override var trainable: Boolean,
        @SerialName("batch_input_shape") override var batchInputShape: List<Int?>?,
        override var dtype: String?,
        var rate: Double
    ) : LayerConfig() {
        @Serializer(forClass = Dropout::class)
        companion object : KSerializer<Dropout> {
            override fun serialize(encoder: Encoder, obj: Dropout) = Unit

            override fun deserialize(decoder: Decoder): Dropout {
                val compositeDecoder = decoder.beginStructure(descriptor)
                var name: String? = null
                var trainable: Boolean? = null
                var batchInputShape: List<Int?>? = null
                var dtype: String? = null
                var rate = 0.0
                mainLoop@ while (true) {
                    when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                        CompositeDecoder.READ_DONE -> break@mainLoop
                        CompositeDecoder.UNKNOWN_NAME -> continue@mainLoop
                        0 -> name = compositeDecoder.decodeStringElement(descriptor, index)
                        1 -> trainable = compositeDecoder.decodeBooleanElement(descriptor, index)
                        2 -> batchInputShape = compositeDecoder.decodeSerializableElement(descriptor, index, Int.serializer().nullable.list.nullable)
                        3 -> dtype = compositeDecoder.decodeStringElement(descriptor, index)
                        4 -> rate = compositeDecoder.decodeDoubleElement(descriptor, index)
                        else -> compositeDecoder.decodeNullableSerializableElement(descriptor, index, JsonObject.serializer().nullable)
                    }
                }
                compositeDecoder.endStructure(descriptor)
                return Dropout(name!!, trainable!!, batchInputShape, dtype, rate)
            }
        }
    }
}
