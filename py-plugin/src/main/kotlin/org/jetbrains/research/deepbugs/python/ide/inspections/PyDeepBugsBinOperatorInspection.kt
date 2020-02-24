package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix.Companion.toLookups
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.datatypes.extractOperatorText
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = CommonModelStorage.common.binOperatorModel

    override val threshold: Float = 0.85f

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : PyDeepBugsBinOpVisitor(holder, session) {

        override fun msg(node: NavigatablePsiElement, vararg params: Any): String {
            val operatorText = (node as PyBinaryExpression).extractOperatorText() ?: ""
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
                val replaceQuickFix = ReplaceBinOperatorQuickFix(data as BinOp, textRange, PyDeepBugsConfig.get().quickFixesThreshold,
                    PyResourceBundle.message("deepbugs.python.replace.operator.family")).takeIf { it.isAvailable() }
                val fixes = listOfNotNull<LocalQuickFix>(PyIgnoreExpressionQuickFix(data, node.text), replaceQuickFix)
                holder.registerProblem(node, msg(node, *replaceQuickFix.toLookups()), ProblemHighlightType.GENERIC_ERROR, *fixes.toTypedArray())
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operator.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperator"
}
