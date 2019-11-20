package org.jetbrains.research.deepbugs.common.datatypes

import org.tensorflow.Tensor

interface DataType {
    fun vectorize(): Tensor<Float>?
}
