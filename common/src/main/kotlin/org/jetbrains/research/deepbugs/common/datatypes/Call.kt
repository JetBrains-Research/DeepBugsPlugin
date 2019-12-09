package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.utils.Mapping

abstract class Call(
    private val callee: String,
    private val arguments: List<String>,
    private val base: String,
    private val argumentTypes: List<String>,
    private val parameters: List<String>,
    @Suppress("unused") private val src: String
) : DataType {
    override val text: String = "$base.$callee(${arguments.joinToString(",")})"

    protected fun vectorize(token: Mapping, type: Mapping): List<Float>? {
        val nameVector = token.get(callee) ?: return null
        val argVectors = arguments.flatMap { arg -> token.get(arg) ?: return null }
        val baseVector = token.get(base) ?: (FloatArray(200) { 0.0f }).toList()
        val typeVectors = argumentTypes.flatMap { argType -> type.get(argType) ?: return null }
        val paramVectors = parameters.flatMap { param -> token.get(param) ?: FloatArray(200) { 0.0f }.toList() }
        return listOf(nameVector, argVectors, baseVector, typeVectors, paramVectors).flatten()
    }
}
