package org.jetbrains.research.deepbugs.services.utils

import com.beust.klaxon.JsonObject

class Mapping(private val json: JsonObject?) {
    fun get(fieldName: String) = json?.array<Float>(fieldName)?.toFloatArray()
}
