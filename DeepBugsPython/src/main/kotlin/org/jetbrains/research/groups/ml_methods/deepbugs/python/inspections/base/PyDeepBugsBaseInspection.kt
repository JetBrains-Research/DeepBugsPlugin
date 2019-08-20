package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.DataType
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.counter.InspectionReportCollector
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.TensorUtils
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

        private fun inspectCodePiece(model: Session?, codePiece: DataType) = codePiece.vectorize()?.let { input ->
            model?.runner()
                    ?.feed("dropout_1_input:0", input)
                    ?.fetch("dense_2/Sigmoid:0")
                    ?.run()
                    ?.firstOrNull()?.let { resTensor -> TensorUtils.getResult(resTensor) }
        }

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                collect(it)?.let { expr ->
                    val result = inspectCodePiece(getModel(), expr)
                    result?.let { res ->
                        if (res > getThreshold()) {
                            registerProblem(node, DeepBugsPythonBundle.message(keyMessage, res),
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            val project = session.file.project
                            InspectionReportCollector.logReport(project, shortName, res)
                        }
                    }
                }
            }
        }
    }
}