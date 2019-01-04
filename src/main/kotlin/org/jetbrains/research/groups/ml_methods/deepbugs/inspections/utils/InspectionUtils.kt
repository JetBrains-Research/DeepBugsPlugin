package org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils

import org.tensorflow.Tensor
import java.nio.FloatBuffer

object InspectionUtils {

    fun vectorizeListOfArrays(arrayList: List<FloatArray>): Tensor<Float> {
        val resArray = arrayList.fold(FloatArray(0)) { acc, array -> acc + array }
        return Tensor.create(longArrayOf(1, resArray.size.toLong()), FloatBuffer.wrap(resArray))
    }

    fun getResult(tensor: Tensor<*>): Float {
        val array = Array(tensor.shape()[0].toInt(), { FloatArray(tensor.shape()[1].toInt()) })
        tensor.copyTo(array)
        return array[0][0]
    }
}