package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.collect

abstract class JSDeepBugsCallExprInspection(
    protected val requiredArgumentsNum: Int,
    threshold: Float = 0.8f
) : JSDeepBugsBaseInspection<JSCallExpression>(threshold) {
    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor = JSDeepBugsCallVisitor(holder)

    inner class JSDeepBugsCallVisitor(holder: ProblemsHolder) : JSDeepBugsVisitor(holder) {
        override fun visitJSCallExpression(node: JSCallExpression?) {
            visit(node ?: return) { node.collect() }
            super.visitJSCallExpression(node)
        }
    }
}
