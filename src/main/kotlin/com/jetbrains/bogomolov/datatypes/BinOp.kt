package com.jetbrains.bogomolov.datatypes

import com.beust.klaxon.Klaxon
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.bogomolov.extraction.extractPyNodeName
import com.jetbrains.bogomolov.extraction.extractPyNodeType
import java.io.File
import com.jetbrains.python.psi.PyBinaryExpression

data class BinOp(val left: String,
                 val right: String,
                 val op: String,
                 val leftType: String,
                 val rightType: String,
                 val parent: String,
                 val grandParent: String,
                 val src: String) {

    companion object {

        /**
         * Method was introduced because [PyBinaryExpression] references only one operator element
         * but there can be at least two (consider `is not`).
         * @param node [PyBinaryExpression] that should be processed.
         * @return [String] with operator text or null if extraction is impossible.
         */
        private fun extractOperatorText(node: PyBinaryExpression): String? {
            var firstElement = node.psiOperator ?: return null
            val lastElement = node.rightExpression?.prevSibling ?: return firstElement.text
            var result = ""
            while(firstElement != lastElement) {
                if (firstElement is PsiWhiteSpace) result += " "
                else result += firstElement.text
                firstElement = firstElement.nextSibling
            }
            return result
        }

        /**
         * Extract information from [PyBinaryExpression] and build [BinOp].
         * @param node [PyBinaryExpression] that should be processed.
         * @return [BinOp] with collected information.
         */
        fun collectFromPyNode(node: PyBinaryExpression, src: String = ""): BinOp? {
            val leftName = extractPyNodeName(node.leftExpression) ?: return null
            val rightName = extractPyNodeName(node.rightExpression) ?: return null
            val op = extractOperatorText(node) ?: return null
            val leftType = extractPyNodeType(node.leftExpression)
            val rightType = extractPyNodeType(node.rightExpression)
            val parent = node.parent.javaClass.simpleName ?: ""
            val grandParent = node.parent.parent.javaClass.simpleName ?: ""
            return BinOp(leftName, rightName, op, leftType, rightType, parent, grandParent, src)
        }

        /**
         * Read json with binary operations from file.
         * @param path path to file.
         * @return list of [BinOp] parsed from file.
         */
        fun readBinOps(path: String) = Klaxon().parseArray<BinOp>(File(path).inputStream())
                ?: throw ParsingFailedException(path)
    }
}


class ParsingFailedException(path: String) : Exception("Unable to parse file at:\n$path")