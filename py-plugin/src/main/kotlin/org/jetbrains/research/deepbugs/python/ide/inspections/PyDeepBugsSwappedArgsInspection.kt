package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.tensorflow.Session

class PyDeepBugsSwappedArgsInspection : PyDeepBugsCallExprInspection() {
    override val model: Session?
        get() = ModelManager.storage.swappedArgsModel
    override val threshold: Float
        get() = PyDeepBugsConfig.get().swappedArgsThreshold

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : PyDeepBugsCallVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String =
            PyResourceBundle.message("deepbugs.python.swapped.args.inspection.warning")

        override fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (result > threshold && !PyDeepBugsConfig.shouldIgnore(data)) {
                holder.registerProblem(node, msg(node), ProblemHighlightType.GENERIC_ERROR, PyIgnoreExpressionQuickFix(data, node.text))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.swapped.args.inspection.display")
}
