package org.jetbrains.research.deepbugs.javascript.ide.inspections.common

import com.intellij.lang.javascript.psi.JSBinaryExpression
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = CommonModelStorage.common.binOperandModel

    override fun createTooltip(node: JSBinaryExpression, vararg params: String): String =
        JSResourceBundle.message(
            "deepbugs.javascript.binary.operand.inspection.warning",
            node.lOperand?.text ?: "",
            node.rOperand?.text ?: ""
        )

    override fun getShortName() = "JSDeepBugsBinOperand"
}
