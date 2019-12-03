package org.jetbrains.research.deepbugs.javascript.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.datatypes.JSBinOp
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.utils.operators
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

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (result > threshold && !JSDeepBugsConfig.shouldIgnore(data)) {
                val textRange = (node as JSBinaryExpression).operationNode!!.textRange
                holder.registerProblem(node, msg(node), ProblemHighlightType.GENERIC_ERROR, JSIgnoreExpressionQuickFix(data, node.text),
                    ReplaceBinOperatorQuickFix(data as JSBinOp, textRange, threshold, JSResourceBundle.message("deepbugs.javascript.display")) { operators[it] ?: "" })
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getShortName() = "JSDeepBugsBinOperator"
}
