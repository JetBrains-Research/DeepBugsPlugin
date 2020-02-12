package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.model.Vocabulary

abstract class BinOp(
    protected val left: String,
    protected val right: String,
    protected val op: String,
    protected val leftType: String,
    protected val rightType: String,
    protected val parent: String,
    protected val grandParent: String,
    @Suppress("unused") protected val src: String
) : DataType {
    override val text: String = "$left $op $right"

    override fun vectorize(): List<Float>? {
        return listOf(
            Vocabulary["tokenToVector"]?.get(left) ?: return null,
            Vocabulary["tokenToVector"]?.get(right) ?: return null,
            Vocabulary["operatorToVector"]?.get(op) ?: return null,
            Vocabulary["typeToVector"]?.get(leftType) ?: return null,
            Vocabulary["typeToVector"]?.get(rightType) ?: return null,
            Vocabulary["nodeTypeToVector"]?.get(parent) ?: return null,
            Vocabulary["nodeTypeToVector"]?.get(grandParent) ?: return null
        ).flatten()
    }

    abstract fun replaceOperator(newOp: String): BinOp
}
