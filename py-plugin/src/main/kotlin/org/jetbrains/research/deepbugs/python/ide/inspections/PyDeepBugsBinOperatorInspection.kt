package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.datatypes.PyBinOp
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.tensorflow.Session

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val model: Session?
        get() = ModelManager.storage.binOperatorModel
    override val threshold: Float
        get() = PyDeepBugsConfig.get().binOperatorThreshold

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : PyDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String = (node as PyBinaryExpression).let {
            PyResourceBundle.message(
                "deepbugs.python.binary.operator.inspection.warning",
                PyBinOp.extractOperatorText(node) ?: ""
            )
        }

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (PyDeepBugsConfig.isProblem(result, threshold, data)) {
                val textRange = (node as PyBinaryExpression).psiOperator!!.textRange
                holder.registerProblem(node, msg(node), ProblemHighlightType.GENERIC_ERROR, PyIgnoreExpressionQuickFix(data, node.text),
                    ReplaceBinOperatorQuickFix(data as PyBinOp, textRange, threshold, PyResourceBundle.message("deepbugs.python.display")))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operator.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperator"
}
