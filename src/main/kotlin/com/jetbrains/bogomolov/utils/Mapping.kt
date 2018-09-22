package com.jetbrains.bogomolov.utils

import com.beust.klaxon.JsonObject

class Mapping(private val json: JsonObject) {
    fun get(fieldName: String) = json.array<Double>(fieldName)
}