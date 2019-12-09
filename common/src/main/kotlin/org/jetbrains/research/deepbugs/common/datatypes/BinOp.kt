package org.jetbrains.research.deepbugs.common.datatypes

import org.jetbrains.research.deepbugs.common.utils.Mapping

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

    protected fun vectorize(token: Mapping, type: Mapping, nodeType: Mapping, operator: Mapping): List<Float>? {
        return listOf(
            token.get(left) ?: return null,
            token.get(right) ?: return null,
            operator.get(op) ?: return null,
            type.get(leftType) ?: return null,
            type.get(rightType) ?: return null,
            nodeType.get(parent) ?: return null,
            nodeType.get(grandParent) ?: return null
        ).flatten()
    }

    abstract fun replaceOperator(newOp: String): BinOp
}
