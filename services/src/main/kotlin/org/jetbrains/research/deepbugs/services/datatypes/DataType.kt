package org.jetbrains.research.deepbugs.services.datatypes

import org.tensorflow.Tensor

interface DataType {
    fun vectorize(): Tensor<Float>?
}
