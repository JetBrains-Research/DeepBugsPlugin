package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElement
import com.intellij.lang.javascript.psi.JSElementVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

abstract class JSDeepBugsBaseInspection<T : JSElement, in V : DataType>(private val threshold: Float) : JSInspection() {
    protected abstract val model: Perceptron?

    protected open fun skip(node: T): Boolean = false

    protected open fun createProblemDescriptor(node: T, data: V): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), myOnTheFly, listOf(JSIgnoreExpressionQuickFix(data, node.text)))

    protected abstract fun createTooltip(node: T, vararg params: String): String

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder) : JSElementVisitor() {
        protected fun visit(node: T, collect: T.() -> V?) {
            if (skip(node)) return
            val data = node.collect() ?: return
            val result = model?.predict(data.vectorize() ?: return) ?: return
            analyzeInspected(result, node, data)
        }

        private fun analyzeInspected(result: Float, node: T, data: V) {
            if (!JSDeepBugsConfig.isProblem(result, threshold, data)) return
            holder.registerProblem(createProblemDescriptor(node, data))
            InspectionReportCollector.logReport(holder.project, shortName, result)
        }
    }
}
