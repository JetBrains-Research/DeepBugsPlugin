package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.model.CommonModelStorage

class Call(
    private val callee: String,
    private val arguments: List<String>,
    private val base: String,
    private val argumentTypes: List<String>,
    private val parameters: List<String>
) : DataType() {
    override val text: String = "$base.$callee(${arguments.joinToString(",")})"

    override fun vectorize(): List<Float>? = CommonModelStorage.vocabulary.let { vocab ->
        val nameVector = vocab.tokens.get(callee) ?: return null
        val argVectors = arguments.flatMap { arg -> vocab.tokens.get(arg) ?: return null }
        val baseVector = vocab.tokens.get(base) ?: (FloatArray(200) { 0.0f }).toList()
        val typeVectors = argumentTypes.flatMap { argType -> vocab.types.get(argType) ?: return null }
        val paramVectors = parameters.flatMap { param -> vocab.tokens.get(param) ?: FloatArray(200) { 0.0f }.toList() }
        listOf(nameVector, argVectors, baseVector, typeVectors, paramVectors).flatten()
    }
}
