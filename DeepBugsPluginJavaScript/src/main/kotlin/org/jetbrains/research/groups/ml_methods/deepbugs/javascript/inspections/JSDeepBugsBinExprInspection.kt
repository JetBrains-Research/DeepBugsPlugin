package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import com.intellij.codeInspection.*
import com.intellij.lang.javascript.inspections.JSInspection
import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.datatypes.JSBinOp
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.InspectionUtils
import org.tensorflow.Session

abstract class JSDeepBugsBinExprInspection : JSInspection() {
    protected abstract val keyMessage: String

    protected abstract fun getThreshold(): Float
    protected abstract fun getModel(): Session?

    override fun createVisitor(
            holder: ProblemsHolder,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(private val holder: ProblemsHolder, session: LocalInspectionToolSession) : JSElementVisitor() {
        override fun visitJSBinaryExpression(node: JSBinaryExpression?) {
            node?.let {
                JSBinOp.collectFromJSNode(it)?.let { binOp ->
                    val result = InspectionUtils.inspectCodePiece(getModel(), binOp)
                    result?.let { res ->
                        if (res > getThreshold())
                            holder.registerProblem(node, DeepBugsPluginServicesBundle.message(keyMessage, res),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                    }
                }
            }
            super.visitJSBinaryExpression(node)
        }
    }
}