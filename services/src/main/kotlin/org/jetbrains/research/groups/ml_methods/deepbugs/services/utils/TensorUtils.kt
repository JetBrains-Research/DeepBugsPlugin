package org.jetbrains.research.groups.ml_methods.deepbugs.services.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.DataType
import org.tensorflow.Session
import org.tensorflow.Tensor

import java.nio.FloatBuffer

object TensorUtils {
    fun inspectCodePiece(model: Session?, codePiece: DataType) = codePiece.vectorize()?.let { input ->
        model?.runner()
                ?.feed("dropout_1_input:0", input)
                ?.fetch("dense_2/Sigmoid:0")
                ?.run()
                ?.firstOrNull()?.let { resTensor -> getResult(resTensor) }
    }

    private fun getResult(tensor: Tensor<*>): Float {
        val array = Array(tensor.shape()[0].toInt()) { FloatArray(tensor.shape()[1].toInt()) }
        tensor.copyTo(array)
        return array[0][0]
    }

    fun vectorizeListOfArrays(arrayList: List<FloatArray>): Tensor<Float> {
        val resArray = arrayList.reduce { acc, array -> acc + array }
        return Tensor.create(longArrayOf(1, resArray.size.toLong()), FloatBuffer.wrap(resArray))
    }
}