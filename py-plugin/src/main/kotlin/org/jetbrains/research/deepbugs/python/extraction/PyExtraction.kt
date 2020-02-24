package org.jetbrains.research.deepbugs.python.extraction

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.siblings
import com.jetbrains.python.*
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
        if (operator == PyTokenTypes.MINUS && operand is PyNumericLiteralExpression) text.asLiteralString()
        operand?.extractNodeName()
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
    is PyReferenceExpression -> qualifier?.extractNodeName() ?: ""
    is PySubscriptionExpression -> operand.extractNodeName() ?: ""
    else -> ""
}

fun PyBinaryExpression.extractOperatorText(): String? {
    operatorRange ?: return null
    if (operatorRange!!.first == operatorRange!!.second) return operatorRange!!.first.text

    return findOperatorElements().toList().asReversed().joinToString(" ") { it.text }
}

fun PyBinaryExpression.findOperatorTextRange(): TextRange? {
    operatorRange ?: return null
    return TextRange(operatorRange!!.first.textRange.startOffset, operatorRange!!.second.textRange.endOffset)
}

private fun PyBinaryExpression.findOperatorElements(): Sequence<PsiElement> {
    operatorRange ?: emptySequence<PsiElement>()
    if (operatorRange!!.first == operatorRange!!.second) return sequenceOf(operatorRange!!.first)

    val siblings = rightExpression?.prevSibling?.siblings(forward = false) ?: emptySequence()
    return siblings.takeWhile { it != operatorRange!!.first.prevSibling }.filter { it !is PsiWhiteSpace }
}

private val PyBinaryExpression.operatorRange: Pair<PsiElement, PsiElement>?
    get() {
        val firstElement = psiOperator ?: return null
        val lastElement = rightExpression?.prevSibling?.siblings(forward = false)?.firstOrNull { it !is PsiWhiteSpace }
        return firstElement to (lastElement ?: firstElement)
    }
