package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.model.CommonModelStorage

class BinOp(
    private val left: String,
    private val right: String,
    private val op: String,
    private val leftType: String,
    private val rightType: String,
    private val parent: String,
    private val grandParent: String
) : DataType() {
    override val text: String = "$left $op $right"

    override fun vectorize(): FloatArray? = CommonModelStorage.vocabulary.let { vocab ->
        listOf(
            vocab.tokens[left] ?: return null,
            vocab.tokens[right] ?: return null,
            vocab.operators[op] ?: return null,
            vocab.types[leftType] ?: return null,
            vocab.types[rightType] ?: return null,
            vocab.nodeTypes[parent] ?: return null,
            vocab.nodeTypes[grandParent] ?: return null
        ).reduce(FloatArray::plus)
    }

    fun replaceOperator(newOp: String) = BinOp(left, right, newOp, leftType, rightType, parent, grandParent)
}
