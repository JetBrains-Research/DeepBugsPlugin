package org.jetbrains.research.groups.ml_methods.deepbugs.services.utils

import org.tensorflow.Tensor
import java.nio.FloatBuffer

object TensorUtils {
    fun vectorizeListOfArrays(arrayList: List<FloatArray>): Tensor<Float> {
        val resArray = arrayList.reduce { acc, array -> acc + array }
        return Tensor.create(longArrayOf(1, resArray.size.toLong()), FloatBuffer.wrap(resArray))
    }
}