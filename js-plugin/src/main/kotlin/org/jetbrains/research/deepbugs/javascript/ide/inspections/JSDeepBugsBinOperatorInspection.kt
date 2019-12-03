package org.jetbrains.research.deepbugs.javascript.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.tensorflow.Session

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val model: Session?
        get() = ModelManager.storage.binOperatorModel
    override val threshold: Float
        get() = JSDeepBugsConfig.get().binOperatorThreshold

    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : JSDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String = (node as JSBinaryExpression).let {
            JSResourceBundle.message(
                "deepbugs.javascript.binary.operator.inspection.warning",
                it.operationNode?.text ?: ""
            )
        }
    }

    override fun getShortName() = "JSDeepBugsBinOperator"
}
