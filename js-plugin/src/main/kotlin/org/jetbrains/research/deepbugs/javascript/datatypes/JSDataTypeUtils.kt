package org.jetbrains.research.deepbugs.javascript.datatypes

import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.util.ObjectUtils
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.javascript.extraction.JSExtractor

fun JSBinaryExpression.collectFromJSNode(): BinOp? {
    val leftName = JSExtractor.extractJSNodeName(lOperand) ?: return null
    val rightName = JSExtractor.extractJSNodeName(rOperand) ?: return null
    val op = operationSign?.toString() ?: return null
    val leftType = JSExtractor.extractJSNodeType(lOperand)
    val rightType = JSExtractor.extractJSNodeType(rOperand)
    val parent = parent.javaClass.simpleName ?: ""
    val grandParent = this.parent.parent.javaClass.simpleName ?: ""
    return BinOp(leftName, rightName, op, leftType, rightType, parent, grandParent)
}

fun JSCallExpression.collectFromJSNode(): Call? {
    val callee = ObjectUtils.tryCast(methodExpression, JSReferenceExpression::class.java) ?: return null
    val name = JSExtractor.extractJSNodeName(callee) ?: return null

    val args = mutableListOf<String>()
    val argTypes = mutableListOf<String>()
    arguments.forEach { arg ->
        JSExtractor.extractJSNodeName(arg)?.let { argName -> args.add(argName) } ?: return null
        JSExtractor.extractJSNodeType(arg).let { argType -> argTypes.add(argType) }
    }

    val base = JSExtractor.extractJSNodeBase(this)

    val resolved = try {
        callee.multiResolve(false).map { it.element }.firstOrNull { it is JSFunction } as? JSFunction
    } catch (ex: Exception) {
        null
    }

    val params = resolved?.parameters?.toList()
    val paramNames = MutableList(args.size) { "" }
    paramNames.forEachIndexed { idx, _ ->
        paramNames[idx] = JSExtractor.extractJSNodeName(params?.getOrNull(idx)) ?: ""
    }
    return Call(name, args, base, argTypes, paramNames)
}
