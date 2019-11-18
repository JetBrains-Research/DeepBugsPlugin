package org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes

import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.Mapping
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.TensorUtils

import org.tensorflow.Tensor

abstract class Call(
    private var callee: String,
    private val arguments: List<String>,
    private val base: String,
    private val argumentTypes: List<String>,
    private var parameters: List<String>,
    private val src: String
) : DataType {

    protected fun vectorize(token: Mapping?, type: Mapping?): Tensor<Float>? {
        val nameVector = token?.get(callee) ?: return null
        val argVectors = arguments.map { arg -> token.get(arg) ?: return null }.reduce { acc, arg -> acc + arg }
        val baseVector = token.get(base) ?: FloatArray(200) { 0.0f }
        val typeVectors = argumentTypes.map { argType -> type?.get(argType) ?: return null }
            .reduce { acc, argType -> acc + argType }
        val paramVectors = parameters.map { param -> token.get(param) ?: FloatArray(200) { 0.0f } }
            .reduce { acc, param -> acc + param }
        return TensorUtils.vectorizeListOfArrays(listOf(nameVector, argVectors, baseVector, typeVectors, paramVectors))
    }
}
