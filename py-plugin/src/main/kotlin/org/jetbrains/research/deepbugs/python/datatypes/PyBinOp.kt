package org.jetbrains.research.deepbugs.python.datatypes

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.python.extraction.PyExtractor
import org.jetbrains.research.deepbugs.python.inspections.base.models
import org.jetbrains.research.deepbugs.common.datatypes.BinOp

class PyBinOp(
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
         * Method was introduced because [PyBinaryExpression] references only one operator element
         * but there can be at least two (consider `is not`).
         * @param node [PyBinaryExpression] that should be processed.
         * @return [String] with operator text or null if extraction is impossible.
         */
        private fun extractOperatorText(node: PyBinaryExpression): String? {
            var firstElement = node.psiOperator ?: return null
            val lastElement = if (node.rightExpression?.prevSibling is PsiWhiteSpace) {
                node.rightExpression?.prevSibling
            } else {
                node.rightExpression
            } ?: return firstElement.text

            var result = ""
            while (firstElement != lastElement) {
                if (firstElement !is PsiWhiteSpace && firstElement !is PsiComment) {
                    if (result != "") result += " "
                    result += firstElement.text
                }
                firstElement = firstElement.nextSibling
            }
            return result
        }

        /**
         * Extract information from [PyBinaryExpression] and build [PyBinOp].
         * @param node [PyBinaryExpression] that should be processed.
         * @return [PyBinOp] with collected information.
         */
        fun collectFromPyNode(node: PyBinaryExpression, src: String = ""): PyBinOp? {
            val leftName = PyExtractor.extractPyNodeName(node.leftExpression) ?: return null
            val rightName = PyExtractor.extractPyNodeName(node.rightExpression) ?: return null
            val op = extractOperatorText(node) ?: return null
            val leftType = PyExtractor.extractPyNodeType(node.leftExpression)
            val rightType = PyExtractor.extractPyNodeType(node.rightExpression)
            val parent = node.parent.javaClass.simpleName ?: ""
            val grandParent = node.parent.parent.javaClass.simpleName ?: ""
            return PyBinOp(leftName, rightName, op, leftType, rightType, parent, grandParent, src)
        }
    }

    override fun vectorize() = vectorize(
        models.storage?.tokenMapping,
        models.storage?.typeMapping,
        models.storage?.nodeTypeMapping,
        models.storage?.operatorMapping
    )
}
