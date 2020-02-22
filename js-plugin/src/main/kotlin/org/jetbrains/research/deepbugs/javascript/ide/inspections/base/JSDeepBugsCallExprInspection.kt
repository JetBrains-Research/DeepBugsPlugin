package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.javascript.datatypes.collect

abstract class JSDeepBugsCallExprInspection : JSDeepBugsBaseInspection() {
    protected open val requiredArgumentsNum: Int? = null

    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor = JSDeepBugsCallVisitor(holder, session)

    inner class JSDeepBugsCallVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : JSDeepBugsVisitor(holder) {
        override fun visitJSCallExpression(node: JSCallExpression?) {
            visit(node ?: return) { node.collect() }
            super.visitJSCallExpression(node)
        }
    }
}
