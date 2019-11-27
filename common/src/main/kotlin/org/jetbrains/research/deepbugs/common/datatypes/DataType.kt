package org.jetbrains.research.deepbugs.common.datatypes

import org.tensorflow.Tensor

interface DataType {
    val text: String

    fun vectorize(): Tensor<Float>?
}
