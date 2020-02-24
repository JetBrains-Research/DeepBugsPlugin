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

    override fun vectorize(): FloatArray? = CommonModelStorage.vocabulary.let { vocab ->
        val nameVector = vocab.tokens[callee] ?: return null
        val argVectors = arguments.fold(FloatArray(0)) { acc, arg -> acc + (vocab.tokens[arg] ?: return null) }
        val baseVector = vocab.tokens[base] ?: FloatArray(200) { 0.0f }
        val typeVectors = argumentTypes.fold(FloatArray(0)) { acc, argType -> acc + (vocab.types[argType] ?: return null) }
        val paramVectors = parameters.fold(FloatArray(0)) { acc, param -> acc + (vocab.tokens[param] ?: FloatArray(200) { 0.0f }) }
        nameVector + argVectors + baseVector + typeVectors + paramVectors
    }
}
