package org.jetbrains.research.groups.ml_methods.deepbugs.extraction

import com.jetbrains.python.PyNames
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.psi.*

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

fun String.asStandardString() = "STD:$this"

object Extractor {
    fun extractPyNodeName(node: PyElement?): String? = when (node) {
        is PyNumericLiteralExpression -> node.text.asLiteralString()
        is PyStringLiteralExpression -> node.stringValue.asLiteralString()
        is PyBoolLiteralExpression -> node.text.asLiteralString()
        is PyNoneLiteralExpression -> node.text.asLiteralString()
        is PyReferenceExpression -> {
            var referencedName = node.referencedName
            referencedName?.let {
                referencedName = when (referencedName) {
                    PyNames.TRUE, PyNames.FALSE, PyNames.NONE -> referencedName?.asLiteralString()
                    else -> referencedName?.asIdentifierString()
                }
            }
            referencedName
        }
        is PyPrefixExpression -> {
            var value: String? = null
            if (node.operator == PyTokenTypes.MINUS) {
                val operand = node.operand
                if (operand is PyNumericLiteralExpression) {
                    val intValue = operand.bigDecimalValue
                    if (intValue != null)
                        value = intValue.negate().toString().asLiteralString()
                }
            }
            value
        }
        is PyCallExpression -> extractPyNodeName(node.callee)
        is PySubscriptionExpression -> extractPyNodeName(node.operand)
        is PyLambdaExpression -> PyNames.LAMBDA.asIdentifierString()
        is PyParameter -> node.text.asIdentifierString()
        else -> null
    }

    fun extractPyNodeType(node: PyElement?): String = when (node) {
        is PyNumericLiteralExpression -> "number"
        is PyStringLiteralExpression -> "string"
        is PyBoolLiteralExpression -> "boolean"
        is PyNoneLiteralExpression -> "none"
        is PyReferenceExpression -> {
            var referencedName = node.referencedName
            referencedName?.let {
                referencedName = when (referencedName) {
                    PyNames.TRUE, PyNames.FALSE -> "boolean"
                    PyNames.CANONICAL_SELF -> "object"
                    else -> "unknown"
                }
            }
            referencedName ?: "unknown"
        }
        is PyPrefixExpression -> extractPyNodeType(node.operand)
        is PyLambdaExpression -> "lambda"
        else -> "unknown"
    }

    fun extractPyNodeBase(node: PyElement?): String? = when (node) {
        is PyCallExpression -> extractPyNodeName(node.firstChild?.firstChild as? PyElement)
        is PySubscriptionExpression -> extractPyNodeType(node.operand)
        else -> ""
    }
}