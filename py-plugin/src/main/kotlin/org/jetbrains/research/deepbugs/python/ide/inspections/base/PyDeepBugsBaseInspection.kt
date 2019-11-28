package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.tensorflow.Session

abstract class PyDeepBugsBaseInspection : PyInspection() {
    protected abstract val model: Session?
    protected abstract val threshold: Float

    abstract inner class PyDeepBugsVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyInspectionVisitor(holder, session) {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = ""): DataType?
        protected abstract fun msg(node: NavigatablePsiElement): String

        private fun createDescriptor(node: NavigatablePsiElement, data: DataType) =
            InspectionManager.getInstance(holder!!.project)
                .createProblemDescriptor(node.navigationElement, msg(node), false,
                    arrayOf(PyIgnoreExpressionQuickFix(data, node.text)), ProblemHighlightType.WARNING)
                .also { it.setTextAttributes(CodeInsightColors.RUNTIME_ERROR) }

        private fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (result > threshold && !PyDeepBugsConfig.shouldIgnore(data)) {
                holder?.registerProblem(createDescriptor(node ,data))
                InspectionReportCollector.logReport(holder!!.project, shortName, result)
            }
        }

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = TensorFlowRunner.inspectCodePiece(model, expr) ?: return
                    analyzeInspected(result, node, expr)
                }
            }
        }
    }
}
