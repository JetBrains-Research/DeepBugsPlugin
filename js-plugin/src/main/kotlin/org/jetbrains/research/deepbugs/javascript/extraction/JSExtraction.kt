package org.jetbrains.research.deepbugs.javascript.extraction

import com.intellij.lang.javascript.JSTokenTypes
import com.intellij.lang.javascript.psi.*
import com.intellij.psi.util.PsiTreeUtil

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

@Suppress("ComplexMethod")
fun JSElement.extractNodeName(): String? = when (this) {
    is JSLiteralExpression -> text.asLiteralString()
    is JSThisExpression -> text.asLiteralString()
    is JSReferenceExpression -> referenceName?.asIdentifierString()
    is JSPrefixExpression -> {
        val operand = expression as? JSLiteralExpression
        if (operand != null && operand.isNumericLiteral && operationSign == JSTokenTypes.MINUS)
            text.asLiteralString()
        else expression?.extractNodeName()
    }
    is JSCallExpression -> methodExpression?.extractNodeName()
    is JSParameter -> text.takeWhile { it != ':' }.asIdentifierString()
    is JSArrayLiteralExpression -> text.asIdentifierString()
    is JSIndexedPropertyAccessExpression ->
        PsiTreeUtil.getChildOfType(this, JSReferenceExpression::class.java)?.referenceName?.asIdentifierString()
    else -> null
}

@Suppress("ComplexMethod")
fun JSElement.extractNodeType(): String = when (this) {
    is JSThisExpression -> "object"
    is JSLiteralExpression -> {
        when {
            value == null -> "null"
            isNumericLiteral -> "number"
            isStringLiteral -> "string"
            isRegExpLiteral -> "regex"
            isBooleanLiteral -> "boolean"
            else -> "unknown"
        }
    }
    is JSReferenceExpression ->
        if (node.elementType.toString() == JSTokenTypes.UNDEFINED_KEYWORD.toString()) "undefined" else "unknown"
    is JSPrefixExpression -> expression?.extractNodeType() ?: "unknown"
    else -> "unknown"
}

fun JSElement.extractNodeBase(): String = when (this) {
    is JSCallExpression -> (methodExpression as? JSReferenceExpression)?.extractNodeBase() ?: ""
    is JSReferenceExpression -> qualifier?.extractNodeName() ?: ""
    else -> ""
}
