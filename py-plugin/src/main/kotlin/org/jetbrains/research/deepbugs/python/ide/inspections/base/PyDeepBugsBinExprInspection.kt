package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.python.datatypes.collect

abstract class PyDeepBugsBinExprInspection : PyDeepBugsBaseInspection() {
    abstract inner class PyDeepBugsBinOpVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyDeepBugsVisitor(holder, session) {
        override fun collect(node: NavigatablePsiElement) = (node as PyBinaryExpression).collect()

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            visitExpr(node)
            super.visitPyBinaryExpression(node)
        }
    }
}
