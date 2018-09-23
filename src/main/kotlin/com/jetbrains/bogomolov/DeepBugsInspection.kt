package com.jetbrains.bogomolov

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.bogomolov.datatypes.BinOp
import com.jetbrains.bogomolov.utils.loadMapping
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork

class DeepBugsInspection : PyInspection() {

    companion object {
        private const val root =  "/home/egor/Work/DeepBugsPlugin"
//        private val model = KerasModelImport.importKerasSequentialModelAndWeights(
//                "$root/models/binOpsDetectionModel.h5")
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
                        registerProblem(node, "Probability: $result", ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
//                        result = model.output(input).getDouble(0)
                    }
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }

}