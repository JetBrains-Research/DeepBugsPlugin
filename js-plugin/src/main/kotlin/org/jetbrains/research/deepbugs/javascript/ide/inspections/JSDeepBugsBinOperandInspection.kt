package org.jetbrains.research.deepbugs.javascript.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.model.ModelStorage
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = ModelStorage["binOperandDetectionModel"]
    override val threshold: Float
        get() = JSDeepBugsConfig.get().binOperandThreshold

    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : JSDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement, vararg params: Any): String = (node as JSBinaryExpression).let {
            JSResourceBundle.message(
                "deepbugs.javascript.binary.operand.inspection.warning",
                it.lOperand?.text ?: "",
                it.rOperand?.text ?: ""
            )
        }

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (JSDeepBugsConfig.isProblem(result, threshold, data)) {
                holder.registerProblem(node, msg(node), ProblemHighlightType.GENERIC_ERROR, JSIgnoreExpressionQuickFix(data, node.text))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getShortName() = "JSDeepBugsBinOperand"
}
