package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import org.tensorflow.Tensor
import java.nio.FloatBuffer
import java.nio.file.Files
import java.nio.file.Paths

object DeepBugsUtils {

    fun toJson(obj: Any): String {
        val sb = StringBuilder(Klaxon().toJsonString(obj))
        return (Parser().parse(sb) as JsonArray<*>).toJsonString(true)
    }

    fun loadMapping(path: String) : Mapping? {
        if (Files.exists(Paths.get(path)))
            return Mapping(Parser().parse(path) as JsonObject)
        return null
    }

    fun vectorizeListOfArrays(arrayList: List<JsonArray<Float>>): Tensor<Float> {
        var resArray = FloatArray(0)
        for (array in arrayList) resArray += array
        return Tensor.create(longArrayOf(1, resArray.size.toLong()), FloatBuffer.wrap(resArray))
    }

    fun getResult(tensor: Tensor<*>): Float {
        val array = Array(tensor.shape()[0].toInt(),  { FloatArray(tensor.shape()[1].toInt()) } )
        tensor.copyTo(array)
        return array[0][0]
    }
}
