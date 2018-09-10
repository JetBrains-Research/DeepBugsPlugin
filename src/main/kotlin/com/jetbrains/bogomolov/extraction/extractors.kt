package com.jetbrains.bogomolov.extraction

import com.jetbrains.python.PyNames
import com.jetbrains.python.psi.*

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

fun String.asStandardString() = "STD:$this"

fun extractPyNodeName(node: PyElement?): String? = when (node) {
    is PyNumericLiteralExpression -> node.text.asLiteralString()
    is PyStringLiteralExpression -> node.stringValue.asLiteralString()
    is PyReferenceExpression -> node.referencedName?.asIdentifierString()
    is PyCallExpression -> extractPyNodeName(node.callee)
    is PySubscriptionExpression -> extractPyNodeName(node.operand)
    is PyLambdaExpression -> PyNames.LAMBDA.asIdentifierString()
    else -> null
}

fun extractPyNodeType(node: PyElement?): String? = when (node) {
    else -> null
}