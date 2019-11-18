package org.jetbrains.research.deepbugs.javascript.inspections.base

import com.intellij.codeInspection.*
import com.intellij.ide.plugins.PluginManager
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.deepbugs.services.datatypes.DataType
import org.jetbrains.research.deepbugs.services.logger.collectors.counter.InspectionReportCollector
import org.jetbrains.research.deepbugs.services.model.ModelStorage
import org.jetbrains.research.deepbugs.services.utils.TensorUtils
import org.tensorflow.Session

//FIXME-review Use `object` inherited from ModelStorage here?
val models by lazy {
    ModelStorage(PluginManager.getPluginByClassName(JSDeepBugsBaseInspection::class.java.name)!!.idString)
}

abstract class JSDeepBugsBaseInspection : JSInspection() {
    protected abstract val keyMessage: String

    protected abstract fun getModel(): Session?
    protected abstract fun getThreshold(): Float

    abstract inner class JSDeepBugsVisitor(private val holder: ProblemsHolder) : JSElementVisitor() {
        protected abstract fun collect(node: NavigatablePsiElement, src: String = ""): DataType?

        protected fun visitExpr(node: NavigatablePsiElement?) {
            node?.let {
                //FIXME-review I am not sure, but it looks like you are here collecting all the PSI items
                //Probably it is possible to find just during visiting? Without explicit traverse.
                collect(it)?.let { expr ->
                    val result = TensorUtils.inspectCodePiece(getModel(), expr)
                    result?.let { res ->
                        if (res > getThreshold()) {
                            holder.registerProblem(node, DeepBugsJSBundle.message(keyMessage, res),
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            InspectionReportCollector.logReport(holder.project, shortName, res)
                        }
                    }
                }
            }
        }
    }
}
