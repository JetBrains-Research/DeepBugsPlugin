package org.jetbrains.research.deepbugs.javascript.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.JSCall

abstract class JSDeepBugsCallExprInspection : JSDeepBugsBaseInspection() {
    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = JSDeepBugsCallVisitor(holder, session)

    inner class JSDeepBugsCallVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder, session) {
        override fun collect(node: NavigatablePsiElement, src: String) = JSCall.collectFromJSNode(node as JSCallExpression)

        override fun visitJSCallExpression(node: JSCallExpression?) {
            visitExpr(node)
            super.visitJSCallExpression(node)
        }
    }
}
