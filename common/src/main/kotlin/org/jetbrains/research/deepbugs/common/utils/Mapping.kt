package org.jetbrains.research.deepbugs.common.utils

import kotlinx.serialization.Serializable

@Serializable
data class Mapping(val data: Map<String, List<Float>>) {
    operator fun get(fieldName: String): List<Float>? = data[fieldName]
}
