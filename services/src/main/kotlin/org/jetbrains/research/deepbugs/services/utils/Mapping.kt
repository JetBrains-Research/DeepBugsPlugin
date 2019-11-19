package org.jetbrains.research.deepbugs.services.utils

import com.squareup.moshi.Moshi
import okio.Buffer
import java.io.FileInputStream
import java.nio.file.Path

class Mapping(jsonFile: Path) {
    private val json: Map<String, List<Float>>

    init {
        val adapter = Moshi.Builder().build().adapter(Map::class.java)
        json = adapter.fromJson(Buffer().readFrom(FileInputStream(jsonFile.toFile()))) as Map<String, List<Float>>
    }

    fun get(fieldName: String): List<Float>? {
        return json[fieldName]
    }
}
