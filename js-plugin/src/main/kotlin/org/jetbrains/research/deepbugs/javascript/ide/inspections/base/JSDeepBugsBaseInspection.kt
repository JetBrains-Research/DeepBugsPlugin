package org.jetbrains.research.deepbugs.javascript.ide.inspections.base

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.tensorflow.Session

abstract class JSDeepBugsBaseInspection : JSInspection() {
    protected abstract val model: Session?
    protected abstract val threshold: Float

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder) : JSElementVisitor() {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = ""): DataType?
        protected abstract fun msg(node: NavigatablePsiElement): String

        protected abstract fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType)

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
