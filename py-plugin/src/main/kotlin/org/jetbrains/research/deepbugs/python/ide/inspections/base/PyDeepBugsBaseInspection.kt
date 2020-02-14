package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.*
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

abstract class PyDeepBugsBaseInspection : PyInspection() {
    protected abstract val model: Perceptron?
    protected abstract val threshold: Float

    abstract inner class PyDeepBugsVisitor(
        holder: ProblemsHolder,
        session: LocalInspectionToolSession
    ) : PyInspectionVisitor(holder, session) {
        protected abstract fun collect(node: NavigatablePsiElement): DataType?
        protected abstract fun msg(node: NavigatablePsiElement, vararg params: Any): String

        protected abstract fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType)

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = model?.predict(expr.vectorize() ?: return) ?: return
                    analyzeInspected(result, node, expr)
                }
            }
        }
    }
}
