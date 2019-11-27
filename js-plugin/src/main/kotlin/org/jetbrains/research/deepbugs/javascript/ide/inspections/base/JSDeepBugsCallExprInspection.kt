package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.javascript.datatypes.JSCall

abstract class JSDeepBugsCallExprInspection : JSDeepBugsBaseInspection() {
    abstract inner class JSDeepBugsCallVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder) {
        override fun collect(node: NavigatablePsiElement, src: String) = JSCall.collectFromJSNode(node as JSCallExpression)

        override fun visitJSCallExpression(node: JSCallExpression?) {
            visitExpr(node)
            super.visitJSCallExpression(node)
        }
    }
}
