package org.jetbrains.research.groups.ml_methods.deepbugs.datatypes

import org.tensorflow.Tensor

interface PyDataType {
    fun vectorize(): Tensor<Float>?
}