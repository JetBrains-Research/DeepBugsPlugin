package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.PathManager
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.groups.ml_methods.deepbugs.datatypes.Call
import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.loadMapping
import org.tensorflow.SavedModelBundle
import org.tensorflow.Tensor
import java.nio.file.Paths

class DeepBugsSwappedArgsInspection : PyInspection() {

    companion object {
        private val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
        private val tokenMapping = loadMapping(Paths.get(root, "models", "tokenToVector.json").toString())
        private val typeMapping = loadMapping(Paths.get(root, "models","typeToVector.json").toString())
        private val model = SavedModelBundle.load(Paths.get(DeepBugsBinExprInspection.root, "models", "swappedArgsDetectionModel").toString(), "serve")
        private val modelSession = model.session()
        private const val keyMessage = "swapped.args.inspection.warning"
    }

    override fun getDisplayName() = DeepBugsPluginBundle.message("swapped.args.inspection.display")
    override fun getShortName(): String = "DeepBugsSwappedArgs"

    private fun getThreshold() = DeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold

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

        override fun visitPyCallExpression(node: PyCallExpression?) {
            node?.let {
                Call.collectFromPyNode(it)?.let { call ->
                    val vector = call.vectorize(tokenMapping, typeMapping)
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
            super.visitPyCallExpression(node)
        }
    }
}