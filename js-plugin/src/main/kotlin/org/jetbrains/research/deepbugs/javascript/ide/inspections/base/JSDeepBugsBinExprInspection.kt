package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.collect

abstract class JSDeepBugsBinExprInspection : JSDeepBugsBaseInspection() {
    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor = JSDeepBugsBinOpVisitor(holder, session)

    open inner class JSDeepBugsBinOpVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder) {
        override fun collect(node: NavigatablePsiElement, src: String) = (node as JSBinaryExpression).collect()

        override fun visitJSBinaryExpression(node: JSBinaryExpression?) {
            visitExpr(node ?: return)
            super.visitJSBinaryExpression(node)
        }
    }
}
