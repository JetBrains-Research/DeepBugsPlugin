package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.javascript.datatypes.collect

abstract class JSDeepBugsBinExprInspection(threshold: Float = 0.8f) : JSDeepBugsBaseInspection<JSBinaryExpression, BinOp>(threshold) {
    override fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor = JSDeepBugsBinOpVisitor(holder)

    inner class JSDeepBugsBinOpVisitor(holder: ProblemsHolder) : JSDeepBugsVisitor(holder) {
        override fun visitJSBinaryExpression(node: JSBinaryExpression?) {
            visit(node ?: return) { node.collect() }
            super.visitJSBinaryExpression(node)
        }
    }
}
