package org.jetbrains.research.deepbugs.python.datatypes

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.python.psi.PyBinaryExpression
import com.jetbrains.python.psi.PyCallExpression
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.python.extraction.PyExtractor

private const val SUPPORTED_ARGS_NUM = 2

fun PyCallExpression.collectFromPyNode(): Call? {
    if (arguments.size != SUPPORTED_ARGS_NUM)
        return null

    val name = PyExtractor.extractPyNodeName(callee) ?: return null

    val args = mutableListOf<String>()
    val argTypes = mutableListOf<String>()
    arguments.forEach { arg ->
        PyExtractor.extractPyNodeName(arg)?.let { argName -> args.add(argName) } ?: return null
        PyExtractor.extractPyNodeType(arg).let { argType -> argTypes.add(argType) }
    }

    val base = PyExtractor.extractPyNodeBase(this)

    val resolved = multiResolveCalleeFunction(PyResolveContext.defaultContext()).firstOrNull()
    var params = resolved?.parameterList?.parameters?.toList()
    if (!params.isNullOrEmpty() && params.first().isSelf && params.size > args.size)
        params = params.drop(1)
    val paramNames = MutableList(args.size) { "" }
    paramNames.forEachIndexed { idx, _ ->
        paramNames[idx] = PyExtractor.extractPyNodeName(params?.getOrNull(idx)) ?: ""
    }
    return Call(name, args, base, argTypes, paramNames)
}

fun PyBinaryExpression.collectFromPyNode(): BinOp? {
    val leftName = PyExtractor.extractPyNodeName(leftExpression) ?: return null
    val rightName = PyExtractor.extractPyNodeName(rightExpression) ?: return null
    val op = extractOperatorText() ?: return null
    val leftType = PyExtractor.extractPyNodeType(leftExpression)
    val rightType = PyExtractor.extractPyNodeType(rightExpression)
    val parent = parent.javaClass.simpleName ?: ""
    val grandParent = this.parent.parent.javaClass.simpleName ?: ""
    return BinOp(leftName, rightName, op, leftType, rightType, parent, grandParent)
}

fun PyBinaryExpression.extractOperatorText(): String? {
    var firstElement = psiOperator ?: return null
    val lastElement = if (rightExpression?.prevSibling is PsiWhiteSpace) {
        rightExpression?.prevSibling
    } else {
        rightExpression
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


