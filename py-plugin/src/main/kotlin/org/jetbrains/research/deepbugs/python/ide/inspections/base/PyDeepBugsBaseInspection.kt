package org.jetbrains.research.deepbugs.python.ide.inspections.base

import com.intellij.codeInspection.*
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.jetbrains.research.deepbugs.common.datatypes.DataType
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

        protected abstract fun analyzeInspected(result: Float, node: NavigatablePsiElement, data: DataType)

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
