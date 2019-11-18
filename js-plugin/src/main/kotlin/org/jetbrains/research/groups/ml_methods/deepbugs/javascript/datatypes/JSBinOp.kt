package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.datatypes

import com.intellij.lang.javascript.psi.JSBinaryExpression

import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.extraction.JSExtractor
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.base.models
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.BinOp

class JSBinOp(
        left: String,
        right: String,
        op: String,
        leftType: String,
        rightType: String,
        parent: String,
        grandParent: String,
        src: String
) : BinOp(left, right, op, leftType, rightType, parent, grandParent, src) {

    companion object {
        /**
         * Extract information from [JSBinaryExpression] and build [JSBinOp].
         * @param node [JSBinaryExpression] that should be processed.
         * @return [JSBinOp] with collected information.
         */
        fun collectFromJSNode(node: JSBinaryExpression, src: String = ""): JSBinOp? {
            val leftName = JSExtractor.extractJSNodeName(node.lOperand) ?: return null
            val rightName = JSExtractor.extractJSNodeName(node.rOperand) ?: return null
            val op = node.operationSign?.toString() ?: return null
            val leftType = JSExtractor.extractJSNodeType(node.lOperand)
            val rightType = JSExtractor.extractJSNodeType(node.rOperand)
            val parent = node.parent.javaClass.simpleName ?: ""
            val grandParent = node.parent.parent.javaClass.simpleName ?: ""
            return JSBinOp(leftName, rightName, op, leftType, rightType, parent, grandParent, src)
        }
    }

    override fun vectorize() = vectorize(models.tokenMapping, models.typeMapping, models.nodeTypeMapping, models.operatorMapping)
}