package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.tensorflow.Session

abstract class JSDeepBugsBaseInspection : JSInspection() {
    protected abstract val model: Session?
    protected abstract val threshold: Float

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder) : JSElementVisitor() {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = ""): DataType?
        protected abstract fun msg(node: NavigatablePsiElement): String

        private fun createDescriptor(node: NavigatablePsiElement, data: DataType) =
            InspectionManager.getInstance(holder.project)
            .createProblemDescriptor(node.navigationElement, msg(node), false,
                arrayOf(JSIgnoreExpressionQuickFix(data, node.text)), ProblemHighlightType.GENERIC_ERROR)
            .also { it.setTextAttributes(CodeInsightColors.RUNTIME_ERROR) }

        private fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (result > threshold && !JSDeepBugsConfig.shouldIgnore(data)) {
                holder.registerProblem(createDescriptor(node ,data))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = TensorFlowRunner.inspectCodePiece(model, expr) ?: return
                    analyzeInspected(result, it, expr)
                }
            }
        }
    }
}
