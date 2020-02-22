package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.collect

abstract class JSDeepBugsBinExprInspection : JSDeepBugsBaseInspection() {
    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor = JSDeepBugsBinOpVisitor(holder, session)

    inner class JSDeepBugsBinOpVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder) {
        override fun visitJSBinaryExpression(node: JSBinaryExpression?) {
            visit(node ?: return) { node.collect() }
            super.visitJSBinaryExpression(node)
        }
    }
}
