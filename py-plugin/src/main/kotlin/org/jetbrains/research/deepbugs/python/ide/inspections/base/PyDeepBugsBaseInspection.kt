package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.*
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix

abstract class PyDeepBugsBaseInspection : PyInspection() {
    protected abstract val model: Perceptron?
    protected abstract val threshold: Float

    abstract inner class PyDeepBugsVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyInspectionVisitor(holder, session) {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = ""): DataType?
        protected abstract fun msg(node: NavigatablePsiElement): String

        private fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (result > threshold && !PyDeepBugsConfig.shouldIgnore(data)) {
                holder?.registerProblem(node, msg(node), ProblemHighlightType.GENERIC_ERROR, PyIgnoreExpressionQuickFix(data, node.text))
                InspectionReportCollector.logReport(holder!!.project, shortName, result)
            }
        }

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = model?.predict(expr.vectorize()) ?: return
                    analyzeInspected(result, node, expr)
                }
            }
        }
    }
}
