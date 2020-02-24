package org.jetbrains.research.deepbugs.python.ide.inspections

import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection(0.86f) {
    override val model: Perceptron?
        get() = CommonModelStorage.common.binOperandModel

    override fun createTooltip(node: PyBinaryExpression, vararg params: Any): String =
        PyResourceBundle.message(
            "deepbugs.python.binary.operand.inspection.warning",
            node.leftExpression.text,
            node.rightExpression?.text ?: ""
        )

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operand.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperand"
}
