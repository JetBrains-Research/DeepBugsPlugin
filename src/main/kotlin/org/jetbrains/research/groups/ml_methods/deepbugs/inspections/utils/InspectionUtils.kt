package org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils

import com.beust.klaxon.JsonArray
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

object InspectionUtils {
    private fun putToRange(array: INDArray, arrDouble: JsonArray<Double>, offset: Int) {
        for (i in 0 until arrDouble.size) {
            array.putScalar(offset + i, arrDouble[i])
        }
    }

    fun vectorizeListOfArrays(arrayList: List<JsonArray<Double>>): INDArray {
        var size = 0
        for (array in arrayList) size += array.size
        val result = Nd4j.create(size)
        var offset = 0
        for (array in arrayList) {
            putToRange(result, array, offset)
            offset += array.size
        }
        return result
    }

    fun getResult(model: MultiLayerNetwork?, input: INDArray): Double? {
        var triesLeft = 10
        var result: Double? = null
        while (result == null && triesLeft > 0) {
            try {
                result = model?.output(input)?.getDouble(0)
            } catch (ex: Exception) {
                triesLeft -= 1
            }
        }
        return result
    }
}