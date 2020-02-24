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

    override fun vectorize(): FloatArray? {
        val vocab = CommonModelStorage.vocabulary

        val nameVector = vocab.tokens[callee] ?: return null
        val argVectors = arguments.map { arg -> vocab.tokens[arg] ?: return null }.reduce(FloatArray::plus)
        val baseVector = vocab.tokens[base] ?: FloatArray(200) { 0.0f }
        val typeVectors = argumentTypes.map { argType -> vocab.types[argType] ?: return null }.reduce(FloatArray::plus)
        val paramVectors = parameters.map { param -> vocab.tokens[param] ?: FloatArray(200) { 0.0f } }.reduce(FloatArray::plus)

        return nameVector + argVectors + baseVector + typeVectors + paramVectors
    }
}
