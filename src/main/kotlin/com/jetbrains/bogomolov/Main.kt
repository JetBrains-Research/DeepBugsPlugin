package com.jetbrains.bogomolov

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport

fun main(args: Array<String>) {
    val path = System.getProperty("user.dir")
    val model = KerasModelImport.importKerasSequentialModelAndWeights(
                "$path/models/binOpsDetectionModel.h5")
    println(model.layerNames)
    println(model.labels)
    model.printConfiguration()
    val parser = Parser()
    val result = parser.parse("$path/models/node_type_to_vector_py.json") as JsonObject
//    val types = parse() as JsonObject
    val arr = result.array<Double>("Assert")
    println(arr!!)
    println(result.string("something"))
}