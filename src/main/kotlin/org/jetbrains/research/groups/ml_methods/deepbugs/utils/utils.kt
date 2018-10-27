package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import java.nio.file.Files
import java.nio.file.Paths

fun toJson(obj: Any): String {
    val sb = StringBuilder(Klaxon().toJsonString(obj))
    return (Parser().parse(sb) as JsonArray<*>).toJsonString(true)
}

fun loadMapping(path: String) = if (Files.exists(Paths.get(path)))
                                    Mapping(Parser().parse(path) as JsonObject)
                                else null
