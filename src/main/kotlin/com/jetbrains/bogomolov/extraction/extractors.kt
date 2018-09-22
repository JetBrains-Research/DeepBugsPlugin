package com.jetbrains.bogomolov.extraction

import com.jetbrains.python.PyNames
import com.jetbrains.python.psi.*

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

fun String.asStandardString() = "STD:$this"

fun extractPyNodeName(node: PyElement?): String? = when (node) {
    is PyNumericLiteralExpression -> node.text.asLiteralString()
    is PyStringLiteralExpression -> node.stringValue.asLiteralString()
    is PyBoolLiteralExpression -> node.text.asLiteralString()
    is PyNoneLiteralExpression -> node.text.asLiteralString()
    is PyReferenceExpression -> {
        var referencedName = node.referencedName
        referencedName?.let {
            referencedName = when(referencedName) {
                "True", "False", "None" -> referencedName?.asLiteralString()
                else -> referencedName?.asIdentifierString()
            }
        }
        referencedName
    }
    is PyCallExpression -> extractPyNodeName(node.callee)
    is PySubscriptionExpression -> extractPyNodeName(node.operand)
    is PyLambdaExpression -> PyNames.LAMBDA.asIdentifierString()
    else -> null
}

fun extractPyNodeType(node: PyElement?): String = when (node) {
    is PyNumericLiteralExpression -> "number"
    is PyStringLiteralExpression -> "string"
    is PyBoolLiteralExpression -> "boolean"
    is PyNoneLiteralExpression -> "null"
    is PyReferenceExpression -> {
        var referencedName = node.referencedName
        referencedName?.let {
            referencedName = when(referencedName) {
                "True", "False" -> "boolean"
                "None" -> "null"
                else -> "unknown"
            }
        }
        referencedName ?: "unknown"
    }
    else -> "unknown"
}