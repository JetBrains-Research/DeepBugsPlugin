package com.jetbrains.deepbugs

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.PathManager
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.deepbugs.datatypes.BinOp
import com.jetbrains.deepbugs.settings.DeepBugsInspectionConfig
import com.jetbrains.deepbugs.utils.loadMapping
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport

class DeepBugsInspection : PyInspection() {

    companion object {
        private val root =  PathManager.getPluginsPath() + "/DeepBugs"
        private val model = KerasModelImport.importKerasSequentialModelAndWeights(
                "$root/models/binOpsDetectionModel.h5")
        private val nodeTypeMapping = loadMapping("$root/models/nodeTypeToVector.json")
        private val tokenMapping = loadMapping("$root/models/tokenToVector.json")
        private val typeMapping = loadMapping("$root/models/typeToVector.json")
        private val operatorMapping = loadMapping("$root/models/operatorToVector.json")
    }

    override fun getDisplayName() = "DeepBugs"

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session)

    class Visitor(holder: ProblemsHolder, session: LocalInspectionToolSession) : PyInspectionVisitor(holder, session) {

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            var result = 0.0

            node?.let {
                BinOp.collectFromPyNode(it)?.let { binOp ->
                    val vector = binOp.vectorize(tokenMapping, nodeTypeMapping, typeMapping, operatorMapping)
                    vector?.let { input ->
                        // TODO catch keras exceptions, but mb rewrite???
                        try {
                            result = model.output(input).getDouble(0)
                        }
                        catch (ex : Exception) {
                            result = 0.0
                        }
                        if (result > DeepBugsInspectionConfig.getInstance().threshold) {
                            registerProblem(node, "Probability: $result", ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                        }
                    }
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }

}