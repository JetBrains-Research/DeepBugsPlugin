package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.jetbrains.research.deepbugs.common.utils.Mapping
import org.tensorflow.Tensor

abstract class BinOp(
    private val left: String,
    private val right: String,
    private val op: String,
    private val leftType: String,
    private val rightType: String,
    private val parent: String,
    private val grandParent: String,
    @Suppress("unused") private val src: String
) : DataType {
    protected fun vectorize(token: Mapping, type: Mapping, nodeType: Mapping, operator: Mapping): Tensor<Float>? {
        return TensorFlowRunner.vectorizeListOfArrays(listOf(
            token.get(left) ?: return null,
            token.get(right) ?: return null,
            operator.get(op) ?: return null,
            type.get(leftType) ?: return null,
            type.get(rightType) ?: return null,
            nodeType.get(parent) ?: return null,
            nodeType.get(grandParent) ?: return null
        ))
    }
}
