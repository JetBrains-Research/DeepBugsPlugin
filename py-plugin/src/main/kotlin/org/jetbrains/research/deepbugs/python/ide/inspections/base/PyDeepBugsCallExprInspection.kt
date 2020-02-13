package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.deepbugs.python.datatypes.collectFromPyNode

abstract class PyDeepBugsCallExprInspection : PyDeepBugsBaseInspection() {
    abstract inner class PyDeepBugsCallVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyDeepBugsVisitor(holder, session) {
        override fun collect(node: NavigatablePsiElement, src: String) = (node as PyCallExpression).collectFromPyNode()

        override fun visitPyCallExpression(node: PyCallExpression?) {
            visitExpr(node)
            super.visitPyCallExpression(node)
        }
    }
}
