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
        val nameVector = vocab.tokens[callee] ?: return null
        val argVectors = arguments.flatMap { arg -> vocab.tokens[arg] ?: return null }
        val baseVector = vocab.tokens[base] ?: List(200) { 0.0f }
        val typeVectors = argumentTypes.flatMap { argType -> vocab.types[argType] ?: return null }
        val paramVectors = parameters.flatMap { param -> vocab.tokens[param] ?: List(200) { 0.0f } }
        listOf(nameVector, argVectors, baseVector, typeVectors, paramVectors).flatten()
    }
}
