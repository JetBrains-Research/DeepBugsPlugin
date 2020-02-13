package org.jetbrains.research.deepbugs.javascript.extraction

import com.intellij.lang.javascript.JSNumberParser
import com.intellij.lang.javascript.JSTokenTypes
import com.intellij.lang.javascript.psi.*
import com.intellij.psi.util.PsiTreeUtil

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

@Suppress("unused")
fun String.asStandardString() = "STD:$this"

@Suppress("ComplexMethod")
object JSExtractor {
    fun extractJSNodeName(node: JSElement?): String? = when (node) {
        is JSLiteralExpression -> node.text.asLiteralString()
        is JSThisExpression -> node.text.asLiteralString()
        is JSReferenceExpression -> node.referenceName?.asIdentifierString()
        is JSPrefixExpression -> {
            val operand = node.expression
            if (node.operationSign == JSTokenTypes.MINUS && operand is JSLiteralExpression && operand.isNumericLiteral)
                JSNumberParser.tryParseNumericValue(operand.significantValue.toString(), false)
                    ?.unaryMinus()?.toString()?.asLiteralString()
            else extractJSNodeName(operand)
        }
        is JSCallExpression -> extractJSNodeName(node.methodExpression)
        is JSParameter -> node.text.takeWhile { it != ':' }.asIdentifierString()
        is JSArrayLiteralExpression -> node.text.toString().asIdentifierString()
        is JSIndexedPropertyAccessExpression -> PsiTreeUtil.getChildOfType(node, JSReferenceExpression::class.java)?.text?.asIdentifierString()
        else -> null
    }

    fun extractJSNodeType(node: JSElement?): String = when (node) {
        is JSThisExpression -> "object"
        is JSLiteralExpression -> {
            when {
                node.value == null -> "null"
                node.isNumericLiteral -> "number"
                node.isStringLiteral -> "string"
                node.isRegExpLiteral -> "regex"
                node.isBooleanLiteral -> "boolean"
                else -> "unknown"
            }
        }
        is JSReferenceExpression ->
            if (node.node.elementType.toString() == JSTokenTypes.UNDEFINED_KEYWORD.toString()) "undefined" else "unknown"
        is JSPrefixExpression -> extractJSNodeType(node.expression)
        else -> "unknown"
    }

    fun extractJSNodeBase(node: JSElement?): String = when (node) {
        is JSCallExpression -> extractJSNodeBase(node.methodExpression as? JSReferenceExpression)
        is JSReferenceExpression -> {
            val base = node.qualifier?.lastChild
            val toExtract = if (base is JSArgumentList) base.prevSibling as? JSElement else base
            when (toExtract?.node?.elementType) {
                JSTokenTypes.LITERALS -> toExtract?.text?.asLiteralString()
                JSTokenTypes.IDENTIFIER -> toExtract?.text?.asIdentifierString()
                else -> extractJSNodeName(toExtract as? JSElement)
            } ?: ""
        }
        else -> ""
    }
}
