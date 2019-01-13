package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.Call
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils.InspectionUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

class DeepBugsSwappedArgsInspection : PyInspection() {
    val keyMessage = "swapped.args.inspection.warning"

    override fun getDisplayName() = DeepBugsPluginBundle.message("swapped.args.inspection.display")
    override fun getShortName(): String = DeepBugsPluginBundle.message("swapped.args.inspection.short.name")

    private fun getThreshold() = DeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold
    private fun getModel() = ModelsManager.swappedArgsModel

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {
        override fun visitPyCallExpression(node: PyCallExpression?) {
            node?.let {
                Call.collectFromPyNode(it)?.let { call ->
                    val result = InspectionUtils.inspectCodePiece(call, getModel())
                    result?.let { res ->
                        if (res > getThreshold())
                            registerProblem(node, DeepBugsPluginBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                    }
                }
            }
            super.visitPyCallExpression(node)
        }
    }
}