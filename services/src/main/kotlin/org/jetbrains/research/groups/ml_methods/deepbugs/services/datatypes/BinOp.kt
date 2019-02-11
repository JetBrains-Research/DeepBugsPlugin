package org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes

import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.InspectionUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.Mapping
import org.tensorflow.Tensor

abstract class BinOp(private val left: String,
                     private val right: String,
                     private val op: String,
                     private val leftType: String,
                     private val rightType: String,
                     private val parent: String,
                     private val grandParent: String,
                     private val src: String) : DataType {

    protected fun vectorize(token: Mapping?, type: Mapping?, nodeType: Mapping?, operator: Mapping?): Tensor<Float>? {
        val leftVector = token?.get(left) ?: return null
        val rightVector = token.get(right) ?: return null
        val leftTypeVector = type?.get(leftType) ?: return null
        val rightTypeVector = type.get(rightType) ?: return null
        val operatorVector = operator?.get(op) ?: return null
        val parentVector = nodeType?.get(parent) ?: return null
        val grandParentVector = nodeType.get(grandParent) ?: return null
        return InspectionUtils.vectorizeListOfArrays(listOf(
                leftVector, rightVector, operatorVector,
                leftTypeVector, rightTypeVector,
                parentVector, grandParentVector))
    }
}