package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.*
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.ide.quickfixes.IgnoreExpressionQuickfix
import org.tensorflow.Session

abstract class PyDeepBugsBaseInspection : PyInspection() {
    protected abstract val keyMessage: String

    protected abstract fun getModel(): Session?
    protected abstract fun getThreshold(): Float

    abstract inner class PyDeepBugsVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyInspectionVisitor(holder, session) {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = ""): DataType?

        private fun analyzeInspected(result: Float, node: NavigatablePsiElement) {
            if (result > getThreshold() && !PyDeepBugsConfig.shouldIgnore(node.text)) {
                holder?.registerProblem(
                    node,
                    PyResourceBundle.message(keyMessage, result),
                    ProblemHighlightType.GENERIC_ERROR,
                    IgnoreExpressionQuickfix(node.text)
                )
                val project = session.file.project
                InspectionReportCollector.logReport(project, shortName, result)
            }
        }

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = TensorFlowRunner.inspectCodePiece(getModel(), expr) ?: return
                    analyzeInspected(result, node)
                }
            }
        }
    }
}
