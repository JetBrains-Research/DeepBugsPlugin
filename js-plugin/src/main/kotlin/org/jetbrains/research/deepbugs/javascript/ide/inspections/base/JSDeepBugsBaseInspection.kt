package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.*
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

abstract class JSDeepBugsBaseInspection : JSInspection() {
    protected abstract val model: Perceptron?
    protected open val threshold: Float = 0.8f

    protected open fun skip(node: NavigatablePsiElement): Boolean = false

    protected abstract fun createProblemDescriptor(node: NavigatablePsiElement, data: DataType): ProblemDescriptor
    protected abstract fun createTooltip(node: NavigatablePsiElement, vararg params: Any): String

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder) : JSElementVisitor() {
        protected abstract fun collect(node: NavigatablePsiElement): DataType?

        protected fun visitExpr(node: NavigatablePsiElement) {
            if (skip(node)) return
            collect(node)?.let {
                val result = model?.predict(it.vectorize() ?: return) ?: return
                analyzeInspected(result, node, it)
            }
        }

        private fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType) {
            if (JSDeepBugsConfig.isProblem(result, threshold, data)) {
                holder.registerProblem(createProblemDescriptor(node, data))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }
}
