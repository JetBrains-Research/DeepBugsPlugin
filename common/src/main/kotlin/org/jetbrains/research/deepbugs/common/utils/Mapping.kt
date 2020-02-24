package org.jetbrains.research.deepbugs.common.utils

import kotlinx.serialization.Serializable

@Serializable
data class Mapping(val data: Map<String, FloatArray>) {
    operator fun get(fieldName: String): FloatArray? = data[fieldName]
}
