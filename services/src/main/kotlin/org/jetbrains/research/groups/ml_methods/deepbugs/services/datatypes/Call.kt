package org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes

import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.TensorUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.Mapping
import org.tensorflow.Tensor

abstract class Call(private val callee: String,
                    private val arguments: Iterable<String>,
                    private val argumentTypes: Iterable<String>,
                    private val base: String,
                    private val parameters: Iterable<String>,
                    private val src: String) : DataType {

    protected fun vectorize(token: Mapping?, type: Mapping?): Tensor<Float>? {
        val nameVector = token?.get(callee) ?: return null
        val argVectors = arguments.map { arg -> token.get(arg) ?: return null }
                .reduce { acc, arg -> acc + arg }
        val typeVectors = argumentTypes.map { argType -> type?.get(argType) ?: return null }
                .reduce { acc, argType -> acc + argType }
        val paramVectors = parameters.map { param -> token.get(param) ?: FloatArray(200) { 0.0f } }
                .reduce { acc, param -> acc + param }
        val baseVector = token.get(base) ?: FloatArray(200) { 0.0f }
        return TensorUtils.vectorizeListOfArrays(listOf(nameVector, argVectors, typeVectors,baseVector, paramVectors))
    }
}