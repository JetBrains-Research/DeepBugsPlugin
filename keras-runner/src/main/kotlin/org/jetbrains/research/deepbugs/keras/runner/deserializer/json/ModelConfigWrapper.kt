package org.jetbrains.research.deepbugs.keras.runner.deserializer.json

import kotlinx.serialization.*
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject

@Serializable
data class ModelConfigWrapper(
    @SerialName("class_name") val className: String,
    val config: ModelConfig
) {
    @Serializer(forClass = ModelConfigWrapper::class)
    companion object : KSerializer<ModelConfigWrapper> {
        fun parse(configString: String) =
            Json(JsonConfiguration.Stable.copy(strictMode = false)).parse(serializer(), configString)

        private fun getModelConfigSerializer(name: String) = when (name) {
            "Sequential" -> ModelConfig.Sequential.serializer()
            else -> throw SerializationException("Not supported")
        }

        override fun serialize(encoder: Encoder, obj: ModelConfigWrapper) = Unit

        override fun deserialize(decoder: Decoder): ModelConfigWrapper {
            val compositeDecoder = decoder.beginStructure(descriptor)
            var className: String? = null
            var config: ModelConfig? = null
            mainLoop@ while (true) {
                when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@mainLoop
                    0 -> className = compositeDecoder.decodeStringElement(descriptor, index)
                    1 -> config = compositeDecoder.decodeSerializableElement(descriptor, index, getModelConfigSerializer(className!!))
                    else -> compositeDecoder.decodeNullableSerializableElement(LayerConfig.Dropout.descriptor, index, JsonObject.serializer().nullable)
                }
            }
            compositeDecoder.endStructure(descriptor)
            return ModelConfigWrapper(className!!, config!!)
        }
    }
}
