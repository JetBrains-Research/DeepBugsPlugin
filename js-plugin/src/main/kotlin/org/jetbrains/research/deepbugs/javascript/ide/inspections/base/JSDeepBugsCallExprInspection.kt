package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.collect

abstract class JSDeepBugsCallExprInspection : JSDeepBugsBaseInspection() {
    protected open val argumentsNum: Int? = null

    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor = JSDeepBugsCallVisitor(holder, session)

    open inner class JSDeepBugsCallVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder) {
        override fun collect(node: NavigatablePsiElement) = (node as JSCallExpression).collect()

        override fun visitJSCallExpression(node: JSCallExpression?) {
            visitExpr(node ?: return)
            super.visitJSCallExpression(node)
        }
    }
}
