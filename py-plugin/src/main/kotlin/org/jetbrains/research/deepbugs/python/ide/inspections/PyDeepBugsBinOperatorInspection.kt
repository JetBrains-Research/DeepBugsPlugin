package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInsight.lookup.LookupElementBuilder
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
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.datatypes.PyBinOp
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = ModelManager.storage.binOperatorModel
    override val threshold: Float
        get() = PyDeepBugsConfig.get().binOperatorThreshold

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : PyDeepBugsBinOpVisitor(holder, session) {

        override fun msg(node: NavigatablePsiElement, vararg params: Any): String {
            val operatorText = PyBinOp.extractOperatorText(node as PyBinaryExpression) ?: ""
            return params.singleOrNull()?.let {
                PyResourceBundle.message(
                    "deepbugs.python.binary.operator.inspection.warning.single",
                    (it as LookupElementBuilder).lookupString,
                    operatorText
                )
            } ?: PyResourceBundle.message("deepbugs.python.binary.operator.inspection.warning", operatorText)
        }

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (PyDeepBugsConfig.isProblem(result, threshold, data)) {
                val textRange = (node as PyBinaryExpression).psiOperator!!.textRange
                val replaceQuickFix = ReplaceBinOperatorQuickFix(data as PyBinOp, textRange, PyDeepBugsConfig.get().quickFixesThreshold,
                    PyResourceBundle.message("deepbugs.python.replace.operator.family"))

                holder.registerProblem(node, msg(node, *replaceQuickFix.lookups.toTypedArray()), ProblemHighlightType.GENERIC_ERROR,
                    PyIgnoreExpressionQuickFix(data, node.text), replaceQuickFix)
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operator.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperator"
}
