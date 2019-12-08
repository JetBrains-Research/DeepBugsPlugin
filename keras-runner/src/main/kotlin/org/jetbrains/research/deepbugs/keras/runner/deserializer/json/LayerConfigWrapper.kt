package org.jetbrains.research.deepbugs.keras.runner.deserializer.json

import kotlinx.serialization.*
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.JsonObject

@Serializable
data class LayerConfigWrapper(
    @SerialName("class_name") var className: String,
    var config: LayerConfig
) {
    @Serializer(forClass = LayerConfigWrapper::class)
    companion object : KSerializer<LayerConfigWrapper> {
        private fun getLayerConfigSerializer(name: String) = when (name) {
            "Dense" -> LayerConfig.Dense.serializer()
            "Dropout" -> LayerConfig.Dropout.serializer()
            else -> throw SerializationException("$name layer is not supported")
        }

        override fun serialize(encoder: Encoder, obj: LayerConfigWrapper) = Unit

        override fun deserialize(decoder: Decoder): LayerConfigWrapper {
            val compositeDecoder = decoder.beginStructure(descriptor)
            var className: String? = null
            var value: LayerConfig? = null
            mainLoop@ while (true) {
                when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@mainLoop
                    0 -> className = compositeDecoder.decodeStringElement(descriptor, index)
                    1 -> value = compositeDecoder.decodeSerializableElement(descriptor, index, getLayerConfigSerializer(className!!))
                    else -> compositeDecoder.decodeNullableSerializableElement(LayerConfig.Dropout.descriptor, index, JsonObject.serializer().nullable)
                }
            }
            compositeDecoder.endStructure(descriptor)
            return LayerConfigWrapper(className!!, value!!)
        }
    }
}
