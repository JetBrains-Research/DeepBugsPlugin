package org.jetbrains.research.deepbugs.python.inspections.base

import com.intellij.codeInspection.*
import com.intellij.ide.plugins.PluginManager
import com.intellij.psi.NavigatablePsiElement
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import org.jetbrains.research.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.deepbugs.services.datatypes.DataType
import org.jetbrains.research.deepbugs.services.logger.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.services.model.ModelManager
import org.jetbrains.research.deepbugs.services.utils.TensorUtils
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

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                //FIXME-review I am not sure, but it looks like you are here collecting all the PSI items
                //Probably it is possible to find just during visiting? Without explicit traverse.
                collect(it)?.let { expr ->
                    val result = TensorUtils.inspectCodePiece(getModel(), expr)
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
