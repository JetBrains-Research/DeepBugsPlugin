package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.python.datatypes.PyBinOp
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.NodeType
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.InspectionUtils

import org.tensorflow.Session

abstract class PyDeepBugsBinExprInspection : PyInspection() {
    protected abstract val keyMessage: String

    protected abstract fun getThreshold(): Float
    protected abstract fun getModel(): Session?

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            node?.let {
                PyBinOp.collectFromPyNode(it)?.let { binOp ->
                    val result = InspectionUtils.inspectCodePiece(getModel(), binOp)
                    result?.let { res ->
                        val threshold = getThreshold()
                        if (res > threshold) {
                            registerProblem(node, DeepBugsPythonBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            val toReport = BugReport(NodeType.BIN_OP.nodeName, shortName, res)
                            DeepBugsPythonService.sendInspectionLog(toReport)
                        }
                    }
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }
}