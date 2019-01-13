package org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.utils

import org.tensorflow.Tensor
import java.nio.FloatBuffer

object TensorUtils {
    fun vectorizeListOfArrays(arrayList: List<FloatArray>): Tensor<Float> {
        val resArray = arrayList.fold(FloatArray(0)) { acc, array -> acc + array }
        return Tensor.create(longArrayOf(1, resArray.size.toLong()), FloatBuffer.wrap(resArray))
    }
}