package org.jetbrains.research.deepbugs.javascript.ide.inspections

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.common.model.ModelStorage
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.datatypes.JSBinOp
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.utils.operators

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = ModelStorage["binOperatorDetectionModel"]
    override val threshold: Float
        get() = JSDeepBugsConfig.get().binOperatorThreshold

    override fun createVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : JSDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement, vararg params: Any): String {
            val operatorText = (node as JSBinaryExpression).operationNode?.text ?: ""
            return params.singleOrNull()?.let {
                JSResourceBundle.message(
                    "deepbugs.javascript.binary.operator.inspection.warning.single",
                    (it as LookupElementBuilder).lookupString,
                    operatorText
                )
            } ?: JSResourceBundle.message("deepbugs.javascript.binary.operator.inspection.warning", operatorText)
        }

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (JSDeepBugsConfig.isProblem(result, threshold, data)) {
                val textRange = (node as JSBinaryExpression).operationNode!!.textRange
                val replaceQuickFix = ReplaceBinOperatorQuickFix(data as JSBinOp, textRange, JSDeepBugsConfig.get().quickFixesThreshold,
                    JSResourceBundle.message("deepbugs.javascript.replace.operator.family")) { operators[it] ?: "" }

                holder.registerProblem(node, msg(node, *replaceQuickFix.lookups.toTypedArray()), ProblemHighlightType.GENERIC_ERROR,
                    JSIgnoreExpressionQuickFix(data, node.text), replaceQuickFix)
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getShortName() = "JSDeepBugsBinOperator"
}
