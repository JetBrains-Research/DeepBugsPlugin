package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.Call
import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.ModelsHolder

class DeepBugsSwappedArgsInspection : PyInspection() {

    val keyMessage = "swapped.args.inspection.warning"

    override fun getDisplayName() = DeepBugsPluginBundle.message("swapped.args.inspection.display")
    override fun getShortName(): String = "DeepBugsSwappedArgs"

    private fun getThreshold() = DeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold
    private fun getModel() = ModelsHolder.swappedArgsModelSession

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        override fun visitPyCallExpression(node: PyCallExpression?) {
            node?.let {
                Call.collectFromPyNode(it)?.let { call ->
                    val vector = call.vectorize(ModelsHolder.tokenMapping, ModelsHolder.typeMapping)
                    vector?.let { input ->
                        val tensor = getModel()?.runner()?.feed("dropout_1_input:0", input)?.fetch("dense_2/Sigmoid:0")?.run()?.firstOrNull()
                        val result = tensor?.let { res -> DeepBugsUtils.getResult(res) } ?: 0.0f
                        if (result > getThreshold()) {
                            registerProblem(node, DeepBugsPluginBundle.message(keyMessage, result),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                        }
                    }
                }
            }
            super.visitPyCallExpression(node)
        }
    }
}