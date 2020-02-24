package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.deepbugs.python.datatypes.collect

abstract class PyDeepBugsCallExprInspection(protected val requiredArgsNum: Int, threshold: Float) : PyDeepBugsBaseInspection<PyCallExpression>(threshold) {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = PyDeepBugsCallVisitor(holder, session)

    inner class PyDeepBugsCallVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyDeepBugsVisitor(holder, session) {
        override fun visitPyCallExpression(node: PyCallExpression?) {
            visit(node ?: return) { node.collect() }
            super.visitPyCallExpression(node)
        }
    }
}
