package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.deepbugs.python.datatypes.PyCall

abstract class PyDeepBugsCallExprInspection : PyDeepBugsBaseInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = PyDeepBugsCallVisitor(holder, session)

    inner class PyDeepBugsCallVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyDeepBugsVisitor(holder, session) {
        override fun collect(node: NavigatablePsiElement, src: String) = PyCall.collectFromPyNode(node as PyCallExpression)

        override fun visitPyCallExpression(node: PyCallExpression?) {
            visitExpr(node)
            super.visitPyCallExpression(node)
        }
    }
}
