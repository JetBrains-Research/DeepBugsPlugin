package org.jetbrains.research.groups.ml_methods.deepbugs.services.utils

import com.beust.klaxon.JsonObject

class Mapping(private val json: JsonObject?) {
    fun get(fieldName: String) = json?.array<Float>(fieldName)?.toFloatArray()
}