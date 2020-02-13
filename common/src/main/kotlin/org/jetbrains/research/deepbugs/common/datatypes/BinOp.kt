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

    override fun vectorize(): List<Float>? = CommonModelStorage.vocabulary.let { vocab ->
        listOf(
            vocab.tokens.get(left) ?: return null,
            vocab.tokens.get(right) ?: return null,
            vocab.operators.get(op) ?: return null,
            vocab.types.get(leftType) ?: return null,
            vocab.types.get(rightType) ?: return null,
            vocab.nodeTypes.get(parent) ?: return null,
            vocab.nodeTypes.get(grandParent) ?: return null
        ).flatten()
    }

    fun replaceOperator(newOp: String) = BinOp(left, right, newOp, leftType, rightType, parent, grandParent)
}
