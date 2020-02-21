package org.jetbrains.research.deepbugs.javascript.datatypes

import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.JSReferenceExpression
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.javascript.extraction.extractNodeBase
import org.jetbrains.research.deepbugs.javascript.extraction.extractNodeName
import org.jetbrains.research.deepbugs.javascript.extraction.extractNodeType

fun JSBinaryExpression.collect(): BinOp? {
    val leftName = lOperand?.extractNodeName() ?: return null
    val rightName = rOperand?.extractNodeName() ?: return null
    val op = operationSign?.toString() ?: return null
    val leftType = lOperand?.extractNodeType() ?: return null
    val rightType = rOperand?.extractNodeType() ?: return null
    val parent = parent.javaClass.simpleName ?: ""
    val grandParent = this.parent.parent.javaClass.simpleName ?: ""
    return BinOp(leftName, rightName, op, leftType, rightType, parent, grandParent)
}

fun JSCallExpression.collect(): Call? {
    val callee = methodExpression as? JSReferenceExpression ?: return null
    val name = callee.extractNodeName() ?: return null

    val args = ArrayList<String>()
    val argTypes = ArrayList<String>()
    arguments.forEach { arg ->
        args.add(arg.extractNodeName() ?: return null)
        argTypes.add(arg.extractNodeType())
    }

    val base = extractNodeBase()

    val resolved = try {
        callee.multiResolve(false).map { it.element }.firstOrNull { it is JSFunction } as? JSFunction
    } catch (ex: Exception) {
        null
    }

    val params = resolved?.parameters?.toList()
    val paramNames = MutableList(args.size) { "" }
    paramNames.forEachIndexed { idx, _ ->
        paramNames[idx] = params?.getOrNull(idx)?.extractNodeName() ?: ""
    }
    return Call(name, args, base, argTypes, paramNames)
}
