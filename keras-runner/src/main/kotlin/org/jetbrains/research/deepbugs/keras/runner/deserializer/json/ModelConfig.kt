package org.jetbrains.research.deepbugs.keras.runner.deserializer.json

import kotlinx.serialization.*
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.JsonObject

@Serializable
sealed class ModelConfig {
    abstract val name: String
    abstract val layers: List<LayerConfigWrapper>

    @Serializable
    data class Sequential(
        override var name: String,
        override var layers: List<LayerConfigWrapper>
    ) : ModelConfig() {
        @Transient
        val batchInputShape = layers.first().config.batchInputShape

        @Serializer(forClass = Sequential::class)
        companion object : KSerializer<Sequential> {
            override fun serialize(encoder: Encoder, obj: Sequential) = Unit

            override fun deserialize(decoder: Decoder): Sequential {
                val compositeDecoder = decoder.beginStructure(descriptor)
                var className: String? = null
                var layers: List<LayerConfigWrapper>? = null
                mainLoop@ while (true) {
                    when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                        CompositeDecoder.READ_DONE -> break@mainLoop
                        0 -> className = compositeDecoder.decodeStringElement(descriptor, index)
                        1 -> layers = compositeDecoder.decodeSerializableElement(descriptor, index, ArrayListSerializer(LayerConfigWrapper.serializer()))
                        else -> compositeDecoder.decodeNullableSerializableElement(LayerConfig.Dropout.descriptor, index, JsonObject.serializer().nullable)
                    }
                }
                compositeDecoder.endStructure(LayerConfigWrapper.descriptor)
                return Sequential(className!!, layers!!)
            }
        }

    }
}
