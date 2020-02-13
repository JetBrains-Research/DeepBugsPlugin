package org.jetbrains.research.deepbugs.python.extraction

import com.jetbrains.python.PyNames
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.psi.*

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

@Suppress("ComplexMethod")
fun PyElement.extractNodeName(): String? = when (this) {
    is PyNumericLiteralExpression -> text.asLiteralString()
    is PyStringLiteralExpression -> stringValue.asLiteralString()
    is PyBoolLiteralExpression -> text.asLiteralString()
    is PyNoneLiteralExpression -> text.asLiteralString()
    is PyReferenceExpression -> referencedName?.asIdentifierString()
    is PyPrefixExpression -> {
        var value: String? = null
        if (operator == PyTokenTypes.MINUS && operand is PyNumericLiteralExpression) {
            val intValue = (operand as PyNumericLiteralExpression).bigDecimalValue
            if (intValue != null) value = intValue.negate().toString().asLiteralString()
        }
        value
    }
    is PyCallExpression -> callee?.extractNodeName()
    is PySubscriptionExpression -> operand.extractNodeName()
    is PyLambdaExpression -> PyNames.LAMBDA.asIdentifierString()
    is PyParameter -> text.substringBefore(':').substringBefore('=').asIdentifierString()
    else -> null
}

@Suppress("ComplexMethod")
fun PyElement.extractNodeType(): String = when (this) {
    is PyNumericLiteralExpression -> "number"
    is PyStringLiteralExpression -> "string"
    is PyBoolLiteralExpression -> "boolean"
    is PyNoneLiteralExpression -> "none"
    is PyReferenceExpression -> when (referencedName) {
        PyNames.CANONICAL_SELF -> "object"
        else -> "unknown"
    }
    is PyPrefixExpression -> operand?.extractNodeType() ?: "unknown"
    is PyLambdaExpression -> "lambda"
    else -> "unknown"
}

fun PyElement.extractNodeBase(): String = when (this) {
    is PyCallExpression -> (callee as? PyReferenceExpression)?.extractNodeBase() ?: ""
    is PyReferenceExpression -> {
        val toExtract = when (val base = qualifier?.lastChild) {
            is PyArgumentList -> base.prevSibling as? PyElement
            else -> base
        }
        (toExtract as? PyElement)?.extractNodeName() ?: ""
    }
    is PySubscriptionExpression -> operand.extractNodeName() ?: ""
    else -> ""
}
