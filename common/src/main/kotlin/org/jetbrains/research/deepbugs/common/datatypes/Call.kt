package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.model.Vocabulary

abstract class Call(
    private val callee: String,
    private val arguments: List<String>,
    private val base: String,
    private val argumentTypes: List<String>,
    private val parameters: List<String>,
    @Suppress("unused") private val src: String
) : DataType {
    override val text: String = "$base.$callee(${arguments.joinToString(",")})"

    override fun vectorize(): List<Float>? {
        val nameVector = Vocabulary["tokenToVector"]?.get(callee) ?: return null
        val argVectors = arguments.flatMap { arg -> Vocabulary["tokenToVector"]?.get(arg) ?: return null }
        val baseVector = Vocabulary["tokenToVector"]?.get(base) ?: (FloatArray(200) { 0.0f }).toList()
        val typeVectors = argumentTypes.flatMap { argType -> Vocabulary["typeToVector"]?.get(argType) ?: return null }
        val paramVectors = parameters.flatMap { param -> Vocabulary["tokenToVector"]?.get(param) ?: FloatArray(200) { 0.0f }.toList() }
        return listOf(nameVector, argVectors, baseVector, typeVectors, paramVectors).flatten()
    }
}
