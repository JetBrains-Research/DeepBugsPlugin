package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.datatypes.JSCall
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSService
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.NodeType
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.InspectionUtils

class JSDeepBugsSwappedArgsInspection : JSInspection() {
    val keyMessage = "swapped.args.inspection.warning"

    override fun getDisplayName() = DeepBugsJSBundle.message("swapped.args.inspection.display")
    override fun getShortName(): String = DeepBugsJSBundle.message("swapped.args.inspection.short.name")

    private fun getThreshold() = JSDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold
    private fun getModel() = DeepBugsJSService.models.swappedArgsModel

    override fun createVisitor(
            holder: ProblemsHolder,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(private val holder: ProblemsHolder, session: LocalInspectionToolSession) : JSElementVisitor() {
        override fun visitJSCallExpression(node: JSCallExpression?) {
            node?.let {
                JSCall.collectFromJSNode(it)?.let { call ->
                    val result = InspectionUtils.inspectCodePiece(getModel(), call)
                    result?.let { res ->
                        if (res > getThreshold()) {
                            holder.registerProblem(node, DeepBugsServicesBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            val toReport = BugReport(NodeType.CALL.nodeName, shortName, res)
                            DeepBugsJSService.sendInspectionLog(toReport)
                        }
                    }
                }
            }
            super.visitJSCallExpression(node)
        }
    }
}