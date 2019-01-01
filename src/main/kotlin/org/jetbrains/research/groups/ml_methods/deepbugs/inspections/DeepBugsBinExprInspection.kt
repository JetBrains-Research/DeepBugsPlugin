package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.BinOp
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils.InspectionUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsHolder
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

abstract class DeepBugsBinExprInspection : PyInspection() {
    protected abstract val keyMessage: String

    protected abstract fun getThreshold(): Double
    protected abstract fun getModel(): MultiLayerNetwork?

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            node?.let {
                BinOp.collectFromPyNode(it)?.let { binOp ->
                    val vector = binOp.vectorize(ModelsHolder.tokenMapping, ModelsHolder.nodeTypeMapping, ModelsHolder.typeMapping, ModelsHolder.operatorMapping)
                    vector?.let { input ->
                        InspectionUtils.getResult(getModel(), input)?.let { res ->
                            if (res > getThreshold()) {
                                registerProblem(node, DeepBugsPluginBundle.message(keyMessage, res),
                                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            }
                        }
                    }
                }
                super.visitPyBinaryExpression(node)
            }
        }
    }
}