package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.lang.javascript.psi.JSCallExpression
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.model.specific.JSSpecificModel
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsIncorrectArgMathInspection : JSDeepBugsMathCallExprInspection(1, 0.5f) {
    override val model: Perceptron?
        get() = JSSpecificModel.math.incorrectArgModel

    override fun createTooltip(node: JSCallExpression, vararg params: Any): String =
        JSResourceBundle.message(
            "deepbugs.javascript.math.incorrect.arg.inspection.warning",
            node.arguments.first().text
        )

    override fun getShortName() = "JSDeepBugsIncorrectArgMath"
}
