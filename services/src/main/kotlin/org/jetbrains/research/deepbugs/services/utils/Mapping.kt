package org.jetbrains.research.deepbugs.services.utils

import com.intellij.util.io.readText
import java.nio.file.Path

class Mapping(jsonFile: Path) {
    private val json: Map<String, List<Float>> by lazy { Json.parse<Map<String, List<Float>>>(jsonFile.readText()) }

    fun get(fieldName: String): List<Float>? = json[fieldName]
}
