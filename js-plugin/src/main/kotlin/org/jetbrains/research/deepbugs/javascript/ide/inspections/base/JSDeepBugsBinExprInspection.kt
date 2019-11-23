package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.JSBinOp

abstract class JSDeepBugsBinExprInspection : JSDeepBugsBaseInspection() {
    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = JSDeepBugsBinOpVisitor(holder, session)

    inner class JSDeepBugsBinOpVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder) {
        override fun collect(node: NavigatablePsiElement, src: String) = JSBinOp.collectFromJSNode(node as JSBinaryExpression)

        override fun visitJSBinaryExpression(node: JSBinaryExpression?) {
            visitExpr(node)
            super.visitJSBinaryExpression(node)
        }
    }
}
