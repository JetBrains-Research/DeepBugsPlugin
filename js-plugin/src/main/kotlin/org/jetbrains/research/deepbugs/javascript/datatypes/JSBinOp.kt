package org.jetbrains.research.deepbugs.javascript.datatypes

import com.intellij.lang.javascript.psi.JSBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.javascript.extraction.JSExtractor

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

    override fun vectorize() = ModelManager.storage?.let { storage ->
        vectorize(storage.tokenMapping, storage.typeMapping, storage.nodeTypeMapping, storage.operatorMapping)
    }
}
