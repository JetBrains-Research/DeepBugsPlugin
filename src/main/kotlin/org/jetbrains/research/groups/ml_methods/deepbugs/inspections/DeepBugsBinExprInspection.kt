package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.PathManager
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.BinOp
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.loadMapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import org.tensorflow.Tensor
import java.nio.file.Paths

abstract class DeepBugsBinExprInspection : PyInspection() {

    protected abstract val model : SavedModelBundle
    protected abstract val modelSession : Session
    protected abstract val keyMessage : String

    companion object {
        val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
        private val nodeTypeMapping = loadMapping(Paths.get(root, "models", "nodeTypeToVector.json").toString())
        private val tokenMapping = loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
        private val typeMapping = loadMapping(Paths.get(root, "models","typeToVector.json").toString())
        private val operatorMapping = loadMapping(Paths.get(root, "models", "operatorToVector.json").toString())
    }

    protected abstract fun getThreshold(): Float

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    inner class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        private fun getResult(tensor: Tensor<*>): Float {
            val array = Array(tensor.shape()[0].toInt(),  { FloatArray(tensor.shape()[1].toInt()) } )
            tensor.copyTo(array)
            return array[0][0]
        }

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            node?.let {
                BinOp.collectFromPyNode(it)?.let { binOp ->
                    val vector = binOp.vectorize(tokenMapping, nodeTypeMapping, typeMapping, operatorMapping)
                    vector?.let { input ->
                        val tensor = modelSession.runner().feed("dropout_1_input:0", input).fetch("dense_2/Sigmoid:0").run()[0]
                        val result = getResult(tensor)
                        if (result > getThreshold()) {
                            registerProblem(node, DeepBugsPluginBundle.message(keyMessage, result),
                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                        }
                    }
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }
}