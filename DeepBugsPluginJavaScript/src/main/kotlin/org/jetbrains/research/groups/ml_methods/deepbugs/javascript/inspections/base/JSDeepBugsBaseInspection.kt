package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.DataType
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.TensorUtils
import org.tensorflow.Session

abstract class JSDeepBugsBaseInspection: JSInspection() {
    protected abstract val keyMessage: String

    protected abstract fun getModel(): Session?
    protected abstract fun getThreshold(): Float

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder, session: LocalInspectionToolSession) : JSElementVisitor() {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = "") : DataType?

        private fun inspectCodePiece(model: Session?, codePiece: DataType) = codePiece.vectorize()?.let { input ->
            model?.runner()?.feed("dropout_1_input:0", input)?.fetch("dense_2/Sigmoid:0")?.run()
                    ?.firstOrNull()?.let { resTensor -> TensorUtils.getResult(resTensor) }
        }

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = inspectCodePiece(getModel(), expr)
                    result?.let { res ->
                        if (res > getThreshold()) {
                            holder.registerProblem(node, DeepBugsJSBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                        }
                    }
                }
            }
        }
    }
}