package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.python.datatypes.PyCall
import org.jetbrains.research.groups.ml_methods.deepbugs.python.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.InspectionUtils

class PyDeepBugsSwappedArgsInspection : PyInspection() {
    val keyMessage = "swapped.args.inspection.warning"

    override fun getDisplayName() = DeepBugsPythonBundle.message("swapped.args.inspection.display")
    override fun getShortName(): String = DeepBugsPythonBundle.message("swapped.args.inspection.short.name")

    private fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold
    private fun getModel() = ModelsManager.swappedArgsModel

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {
        override fun visitPyCallExpression(node: PyCallExpression?) {
            node?.let {
                PyCall.collectFromPyNode(it)?.let { call ->
                    val result = InspectionUtils.inspectCodePiece(getModel(), call)
                    result?.let { res ->
                        if (res > getThreshold())
                            registerProblem(node, DeepBugsPluginServicesBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                    }
                }
            }
            super.visitPyCallExpression(node)
        }
    }
}