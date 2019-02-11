package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.python.datatypes.PyCall
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.NodeType
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.InspectionUtils

class PyDeepBugsSwappedArgsInspection : PyInspection() {
    val keyMessage = "swapped.args.inspection.warning"

    override fun getDisplayName() = DeepBugsPythonBundle.message("swapped.args.inspection.display")
    override fun getShortName(): String = DeepBugsPythonBundle.message("swapped.args.inspection.short.name")

    private fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold
    private fun getModel() = DeepBugsPythonService.models.swappedArgsModel

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
                        val threshold = getThreshold()
                        if (res > threshold) {
                            registerProblem(node, DeepBugsPythonBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            val toReport = BugReport(NodeType.CALL, shortName, res)
                            DeepBugsPythonService.sendInspectionLog(toReport)
                        }
                    }
                }
            }
            super.visitPyCallExpression(node)
        }
    }
}