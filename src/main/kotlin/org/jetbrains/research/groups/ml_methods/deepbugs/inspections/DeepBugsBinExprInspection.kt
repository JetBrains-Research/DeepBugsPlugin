package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.BinOp
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.utils.InspectionUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.tensorflow.Session

abstract class DeepBugsBinExprInspection : PyInspection() {
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
                BinOp.collectFromPyNode(it)?.let { binOp ->
                    val vector = binOp.vectorize(ModelsManager.tokenMapping, ModelsManager.nodeTypeMapping,
                            ModelsManager.typeMapping, ModelsManager.operatorMapping)
                    vector?.let { input ->
                        val resTensor = getModel()?.runner()
                                     ?.feed("dropout_1_input:0", input)
                                     ?.fetch("dense_2/Sigmoid:0")
                                     ?.run()?.firstOrNull()
                        resTensor?.let { tensor ->
                            val result = InspectionUtils.getResult(tensor)
                            result.let { res ->
                                if (res > getThreshold()) {
                                    registerProblem(node, DeepBugsPluginBundle.message(keyMessage, res),
                                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                                }
                            }
                        }
                    }
                }
                super.visitPyBinaryExpression(node)
            }
        }
    }
}