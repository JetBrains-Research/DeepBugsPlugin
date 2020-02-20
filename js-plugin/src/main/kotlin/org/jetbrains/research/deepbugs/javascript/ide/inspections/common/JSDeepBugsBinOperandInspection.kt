package org.jetbrains.research.deepbugs.javascript.ide.inspections.common

import com.intellij.lang.javascript.psi.JSBinaryExpression
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = CommonModelStorage.common.binOperandModel

    override fun createTooltip(node: NavigatablePsiElement, vararg params: Any): String = (node as JSBinaryExpression).let {
        JSResourceBundle.message(
            "deepbugs.javascript.binary.operand.inspection.warning",
            it.lOperand?.text ?: "",
            it.rOperand?.text ?: ""
        )
    }

    override fun getShortName() = "JSDeepBugsBinOperand"
}
