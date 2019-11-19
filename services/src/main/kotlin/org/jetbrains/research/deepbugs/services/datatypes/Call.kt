package org.jetbrains.research.deepbugs.services.datatypes

import org.jetbrains.research.deepbugs.services.utils.Mapping
import org.jetbrains.research.deepbugs.services.utils.TensorUtils

import org.tensorflow.Tensor

//FIXME-review you can make it not abstract class, but data class with a lambda that will be used if set to override vectorize
abstract class Call(
    private var callee: String,
    private val arguments: List<String>,
    private val base: String,
    private val argumentTypes: List<String>,
    private var parameters: List<String>,
    @Suppress("unused") private val src: String
) : DataType {

    protected fun vectorize(token: Mapping?, type: Mapping?): Tensor<Float>? {
        val nameVector = token?.get(callee) ?: return null
        val argVectors = arguments.map { arg -> token.get(arg) ?: return null }.reduce { acc, arg -> acc + arg }
        val baseVector = token.get(base) ?: (FloatArray(200) { 0.0f }).toList()
        val typeVectors = argumentTypes.map { argType -> type?.get(argType) ?: return null }
            .reduce { acc, argType -> acc + argType }
        val paramVectors = parameters.map { param -> token.get(param) ?: FloatArray(200) { 0.0f }.toList() }
            .reduce { acc, param -> acc + param }
        return TensorUtils.vectorizeListOfArrays(listOf(nameVector, argVectors, baseVector, typeVectors, paramVectors))
    }
}
