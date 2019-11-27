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

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val model: Session?
        get() = ModelManager.storage.binOperandModel
    override val threshold: Float
        get() = JSDeepBugsConfig.get().binOperandThreshold

    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : JSDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String? = (node as? JSBinaryExpression)?.let {
                JSResourceBundle.message(
                    "deepbugs.javascript.binary.operand.inspection.warning",
                    it.lOperand?.text ?: "",
                    it.rOperand?.text ?: ""
                )
        }
    }

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.binary.operand.inspection.display")
    override fun getShortName() = "JSOperandInspection"
}
