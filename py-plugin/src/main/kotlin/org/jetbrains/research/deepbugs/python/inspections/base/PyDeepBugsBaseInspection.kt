package org.jetbrains.research.deepbugs.python.inspections.base

import com.intellij.codeInspection.*
import com.intellij.ide.plugins.PluginManager
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.logger.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.common.TensorFlowRunner
import org.tensorflow.Session

val models by lazy {
    object : ModelManager() {
        override val pluginName: String =
            PluginManager.getPluginByClassName(PyDeepBugsBaseInspection::class.java.name)!!.idString
    }
}

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
            if (result > getThreshold()) {
                registerProblem(node, DeepBugsPythonBundle.message(keyMessage, result),
                    ProblemHighlightType.GENERIC_ERROR)
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
