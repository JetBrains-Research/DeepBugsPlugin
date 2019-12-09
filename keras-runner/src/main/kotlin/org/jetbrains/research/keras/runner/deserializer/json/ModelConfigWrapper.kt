package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
sealed class ModelConfigWrapper {
    abstract val config: ModelConfig

    @Serializable
    @SerialName("Sequential")
    data class SequentialConfigWrapper(override val config: ModelConfig.Sequential): ModelConfigWrapper()

    companion object {
        private val json =
            Json(configuration = JsonConfiguration.Stable.copy(strictMode = false, classDiscriminator = "class_name"))

        fun parse(configString: String) = json.parse(serializer(), configString)
    }
}
