package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.python.datatypes.PyBinOp

abstract class PyDeepBugsBinExprInspection : PyDeepBugsBaseInspection() {
    abstract inner class PyDeepBugsBinOpVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyDeepBugsVisitor(holder, session) {
        override fun collect(node: NavigatablePsiElement, src: String) = PyBinOp.collectFromPyNode(node as PyBinaryExpression)

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            visitExpr(node)
            super.visitPyBinaryExpression(node)
        }
    }
}
