package org.jetbrains.research.keras.runner.deserializer.json

import kotlinx.serialization.*

@Serializable
@Suppress("EnumEntryName")
enum class ActivationType {
    relu,
    sigmoid
}
