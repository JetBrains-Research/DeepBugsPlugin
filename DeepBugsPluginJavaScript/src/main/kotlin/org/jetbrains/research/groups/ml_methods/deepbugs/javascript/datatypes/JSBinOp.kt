package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.datatypes

import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiWhiteSpace
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.extraction.Extractor
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSService
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.BinOp

class JSBinOp(left: String,
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
         * Method was introduced because [JSBinaryExpression] references only one operator element
         * but there can be at least two (consider `is not`).
         * @param node [JSBinaryExpression] that should be processed.
         * @return [String] with operator text or null if extraction is impossible.
         */
        private fun extractOperatorText(node: JSBinaryExpression): String? {
            var firstElement = node.operationSign ?: return null
            val lastElement = if (node.rOperand?.prevSibling is PsiWhiteSpace) {
                node.rOperand?.prevSibling
            } else {
                node.rOperand
            } ?: return firstElement.toString()

            var result = ""
            while (firstElement != lastElement) {
                if (firstElement !is PsiWhiteSpace && firstElement !is PsiComment) {
                    if (result != "") result += " "
                    result += firstElement.toString()
                }
                //firstElement = firstElement //????
            }
            return result
        }

        /**
         * Extract information from [JSBinaryExpression] and build [JSBinOp].
         * @param node [JSBinaryExpression] that should be processed.
         * @return [JSBinOp] with collected information.
         */
        fun collectFromJSNode(node: JSBinaryExpression, src: String = ""): JSBinOp? {
            val leftName = Extractor.extractJSNodeName(node.lOperand)
                    ?: return null
            val rightName = Extractor.extractJSNodeName(node.rOperand)
                    ?: return null
            val op = extractOperatorText(node)
                    ?: return null
            val leftType = Extractor.extractJSNodeType(node.lOperand)
            val rightType = Extractor.extractJSNodeType(node.rOperand)
            val parent = node.parent.javaClass.simpleName ?: ""
            val grandParent = node.parent.parent.javaClass.simpleName ?: ""
            return JSBinOp(leftName, rightName, op, leftType, rightType, parent, grandParent, src)
        }
    }

    override fun vectorize() = DeepBugsJSService.models.let { storage ->
        vectorize(storage.tokenMapping, storage.typeMapping, storage.nodeTypeMapping, storage.operatorMapping)
    }
}