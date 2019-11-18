package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.python.datatypes.PyBinOp

abstract class PyDeepBugsBinExprInspection : PyDeepBugsBaseInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = PyDeepBugsBinOpVisitor(holder, session)

    inner class PyDeepBugsBinOpVisitor(
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
