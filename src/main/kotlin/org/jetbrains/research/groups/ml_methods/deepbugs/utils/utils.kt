package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser

fun toJson(obj: Any): String {
    val sb = StringBuilder(Klaxon().toJsonString(obj))
    return (Parser().parse(sb) as JsonArray<*>).toJsonString(true)
}

fun loadMapping(path: String) = Mapping(Parser().parse(path) as JsonObject)
