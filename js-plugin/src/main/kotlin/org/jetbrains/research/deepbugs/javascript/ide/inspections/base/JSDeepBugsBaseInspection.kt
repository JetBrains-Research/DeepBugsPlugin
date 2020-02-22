package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.*
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElement
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

abstract class JSDeepBugsBaseInspection : JSInspection() {
    protected abstract val model: Perceptron?
    protected open val threshold: Float = 0.8f

    protected open fun skip(node: PsiElement): Boolean = false

    protected open fun createProblemDescriptor(node: PsiElement, data: DataType): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), listOf(JSIgnoreExpressionQuickFix(data, node.text)))

    protected abstract fun createTooltip(node: PsiElement, vararg params: Any): String

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder) : JSElementVisitor() {
        protected fun visit(node: JSElement, collect: JSElement.() -> DataType?) {
            if (skip(node)) return
            node.collect()?.let {
                val result = model?.predict(it.vectorize() ?: return) ?: return
                analyzeInspected(result, node, it)
            }
        }

        private fun analyzeInspected(result: Float, node: JSElement, data: DataType) {
            if (JSDeepBugsConfig.isProblem(result, threshold, data)) {
                holder.registerProblem(createProblemDescriptor(node, data))
                InspectionReportCollector.logReport(holder.project, shortName, result)
            }
        }
    }
}
