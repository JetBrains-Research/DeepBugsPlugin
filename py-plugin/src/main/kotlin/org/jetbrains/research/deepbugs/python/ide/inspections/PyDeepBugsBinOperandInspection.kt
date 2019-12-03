package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.tensorflow.Session

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val model: Session?
        get() = ModelManager.storage.binOperandModel
    override val threshold: Float
        get() = PyDeepBugsConfig.get().binOperandThreshold

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : PyDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String = (node as PyBinaryExpression).let {
            PyResourceBundle.message(
                "deepbugs.python.binary.operand.inspection.warning",
                it.leftExpression.text,
                it.rightExpression?.text ?: ""
            )
        }

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (result > threshold && !PyDeepBugsConfig.shouldIgnore(data)) {
                holder.registerProblem(node, msg(node), ProblemHighlightType.GENERIC_ERROR, PyIgnoreExpressionQuickFix(data, node.text))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operand.inspection.display")
}
