package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.python.datatypes.collect

abstract class PyDeepBugsBinExprInspection(threshold: Float) : PyDeepBugsBaseInspection<PyBinaryExpression, BinOp>(threshold) {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ) = PyDeepBugsBinOpVisitor(holder, session)

    inner class PyDeepBugsBinOpVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyDeepBugsVisitor(holder, session) {
        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            visit(node ?: return) { node.collect() }
            super.visitPyBinaryExpression(node)
        }
    }
}
