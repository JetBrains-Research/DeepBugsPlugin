package com.jetbrains.bogomolov

import com.intellij.codeInspection.LocalInspectionToolSession
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
        private val root =  System.getProperty("user.dir")
        private val model = KerasModelImport.importKerasSequentialModelAndWeights(
                "$root/models/binOpsDetectionModel.h5")
        private val nodeTypeMapping = loadMapping("$root/models/nodeTypeToVector.json")
        private val tokenMapping = loadMapping("$root/models/tokenToVector.json")
        private val typeMapping = loadMapping("$root/models/typeToVector.json")
    }

    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor = Visitor(holder, session, model)

    class Visitor(holder: ProblemsHolder,
                  session: LocalInspectionToolSession,
                  model: MultiLayerNetwork) : PyInspectionVisitor(holder, session) {

        override fun visitPyBinaryExpression(node: PyBinaryExpression?) {
            node?.let {
                BinOp.collectFromPyNode(it)?.let { binOp ->
                    val vector = binOp.vectorize()
                    vector?.let { input ->
                        val result = model.output(input)
                    }
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }

}