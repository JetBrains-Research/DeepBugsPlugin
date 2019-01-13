package org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.PyDataType
import org.tensorflow.Session
import org.tensorflow.Tensor

object InspectionUtils {

    private fun getResult(tensor: Tensor<*>): Float {
        val array = Array(tensor.shape()[0].toInt(), { FloatArray(tensor.shape()[1].toInt()) })
        tensor.copyTo(array)
        return array[0][0]
    }

    fun inspectCodePiece(codePiece: PyDataType, model: Session?) = codePiece.vectorize()?.let { input ->
        model?.runner()?.feed("dropout_1_input:0", input)?.fetch("dense_2/Sigmoid:0")?.run()
                ?.firstOrNull()?.let { resTensor -> getResult(resTensor) }
    }
}