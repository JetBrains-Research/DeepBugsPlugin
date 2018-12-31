package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.intellij.ide.ui.AppearanceOptionsTopHitProvider
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import java.nio.file.Files
import java.nio.file.Paths

object JsonUtils {
    fun toJson(obj: Any): String {
        val sb = StringBuilder(Klaxon().toJsonString(obj))
        return (Parser().parse(sb) as JsonArray<*>).toJsonString(true)
    }

    fun loadMapping(path: String): Mapping? {
        if (Files.exists(Paths.get(path)))
            return Mapping(Parser().parse(path) as JsonObject)
        return null
    }
}
