package com.jetbrains.deepbugs

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.options.Configurable
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.deepbugs.datatypes.BinOp
import com.jetbrains.deepbugs.gui.DeepBugsGUI
import com.jetbrains.deepbugs.utils.loadMapping
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import javax.swing.JComponent

// TODO way to save customized plugin settings?
class DeepBugsInspection : PyInspection(), Configurable {

    companion object {
        private var threshold = 0.89
        // TODO rewrite
        private const val root =  "/Users/username/DeepBugsPlugin"
        private val model = KerasModelImport.importKerasSequentialModelAndWeights(
                "$root/models/binOpsDetectionModel.h5")
        private val nodeTypeMapping = loadMapping("$root/models/nodeTypeToVector.json")
        private val tokenMapping = loadMapping("$root/models/tokenToVector.json")
        private val typeMapping = loadMapping("$root/models/typeToVector.json")
        private val operatorMapping = loadMapping("$root/models/operatorToVector.json")
    }

    private var deepBugsGUI : DeepBugsGUI? = null

    override fun getDisplayName() = "DeepBugs"

    override fun isModified() = deepBugsGUI!!.threshold != threshold

    override fun apply() {
        threshold = deepBugsGUI!!.threshold
    }

    override fun reset() {
        deepBugsGUI!!.threshold = threshold
    }

    override fun disposeUIResources() {
        deepBugsGUI = null
    }

    override fun createComponent() : JComponent? {
        deepBugsGUI = DeepBugsGUI()
        deepBugsGUI!!.threshold = threshold
        return deepBugsGUI!!.rootPanel
    }

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
                        // TODO fix null input exception while reinspecting the code
                        result = model.output(input).getDouble(0)
                        if (result > threshold) {
                            registerProblem(node, "Probability: $result", ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                        }
                    }
                }
            }
            super.visitPyBinaryExpression(node)
        }
    }

}