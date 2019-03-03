package org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes

import org.tensorflow.Tensor

interface DataType {
    fun vectorize(): Tensor<Float>?
}