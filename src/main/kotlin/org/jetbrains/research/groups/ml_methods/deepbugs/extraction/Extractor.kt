package org.jetbrains.research.groups.ml_methods.deepbugs.extraction

import com.intellij.ide.actions.QualifiedNameProviderUtil
import com.intellij.psi.util.QualifiedName
import com.jetbrains.python.PyNames
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.hierarchy.call.PyCallHierarchyBrowser
import com.jetbrains.python.hierarchy.call.PyCallerFunctionTreeStructure
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.impl.PyCallExpressionHelper
import com.jetbrains.python.psi.impl.PyCallExpressionNavigator
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference

fun String.asLiteralString() = "LIT:$this"

fun String.asIdentifierString() = "ID:$this"

fun String.asStandardString() = "STD:$this"

object Extractor {
    fun extractPyNodeName(node: PyElement?): String? = when (node) {
        is PyNumericLiteralExpression -> node.text.asLiteralString()
        is PyStringLiteralExpression -> node.stringValue.asLiteralString()
        is PyBoolLiteralExpression -> node.text.asLiteralString()
        is PyNoneLiteralExpression -> node.text.asLiteralString()
        is PyReferenceExpression -> node.referencedName?.asIdentifierString()
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
        is PyParameter -> node.text.substringBefore(":").asIdentifierString()
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

    fun extractPyNodeBase(node: PyElement?): String = when (node) {
        is PyCallExpression -> node.callee?.let { callee ->
            var qualifier = ""
            if (callee is PyQualifiedExpression)
                qualifier = extractPyNodeName(callee.qualifier?.lastChild?.parent as? PyElement) ?: ""
            qualifier
        } ?: ""
        is PySubscriptionExpression -> extractPyNodeName(node.operand) ?: ""
        else -> ""
    }
}