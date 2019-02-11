package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.extraction

import com.intellij.lang.javascript.JSNumberParser
import com.intellij.lang.javascript.JSTokenTypes
import com.intellij.lang.javascript.psi.*

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

fun String.asStandardString() = "STD:$this"

object Extractor {
    fun extractJSNodeName(node: JSElement?): String? = when (node) {
        is JSLiteralExpression -> node.text.asLiteralString()
        is JSReferenceExpression -> node.referenceName?.asIdentifierString()
        is JSPrefixExpression -> {
            if (node.operationSign == JSTokenTypes.MINUS) {
                val operand = node.expression
                if (operand is JSLiteralExpression && operand.isNumericLiteral)
                    JSNumberParser.tryParseBigInt(operand.significantValue)?.negate().toString().asLiteralString()
                else null
            } else null
        }
        is JSCallExpression -> extractJSNodeName(node.methodExpression)
        is JSParameter -> node.text.asIdentifierString()
        else -> null
    }

    fun extractJSNodeType(node: JSElement?): String = when (node) {
        is JSThisExpression -> "object"
        is JSLiteralExpression -> {
            when (node.getExpressionKind(false)) {
                JSLiteralExpressionKind.BOOLEAN -> "boolean"
                JSLiteralExpressionKind.REGEXP -> "regex"
                JSLiteralExpressionKind.QUOTED -> "string"
                else -> if (node.isNumericLiteral) "number"
                else "unknown"
            }
        }
        is JSPrefixExpression -> extractJSNodeType(node.expression)
        else -> "unknown"
    }

    fun extractPyNodeBase(node: JSElement?): String = ""
}