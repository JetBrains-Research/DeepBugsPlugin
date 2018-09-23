package com.jetbrains.bogomolov.datatypes

import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.bogomolov.extraction.extractPyNodeName
import com.jetbrains.bogomolov.extraction.extractPyNodeType
import com.jetbrains.bogomolov.utils.Mapping
import java.io.File
import com.jetbrains.python.psi.PyBinaryExpression
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

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

    private fun putToRange(array: INDArray, arrDouble: JsonArray<Double>, offset: Int) {
        for (i in 0 until arrDouble.size) {
            array.putScalar(offset + i, arrDouble[offset + i])
        }
    }

    private fun vectorizeListOfArrays(arrayList: List<JsonArray<Double>>): INDArray {
        var size = 0
        for (array in arrayList) size += array.size
        val result = Nd4j.create(size)
        var offset = 0
        for (array in arrayList) {
            putToRange(result, array, offset)
            offset += array.size
        }
        return result
    }

    fun vectorize(token: Mapping, nodeType: Mapping, type: Mapping, operator: Mapping): INDArray? {
        val leftVector = token.get(left) ?: return null
        val rightVector = token.get(right) ?: return null
        val leftTypeVector = type.get(leftType) ?: return null
        val rightTypeVector = type.get(rightType) ?: return null
        val operatorVector = operator.get(op) ?: return null
        val parentVector = nodeType.get(parent) ?: return null
        val grandParentVector = nodeType.get(grandParent) ?: return null
        return vectorizeListOfArrays(listOf(
                leftVector, rightVector,
                operatorVector,
                leftTypeVector, rightTypeVector,
                parentVector, grandParentVector))
    }
}


class ParsingFailedException(path: String) : Exception("Unable to parse file at:\n$path")