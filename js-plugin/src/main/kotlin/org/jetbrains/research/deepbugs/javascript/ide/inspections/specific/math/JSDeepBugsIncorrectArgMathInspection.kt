package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.model.JSModelStorage
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsIncorrectArgMathInspection : JSDeepBugsMathCallExprInspection() {
    override val requiredArgumentsNum: Int? = 1
    //TODO: should it really be that low?
    override val threshold: Float = 0.5f

    override val model: Perceptron?
        get() = JSModelStorage.specific.math.incorrectArgModel

    override fun createTooltip(node: NavigatablePsiElement, vararg params: Any): String =
        JSResourceBundle.message(
            "deepbugs.javascript.math.incorrect.arg.inspection.warning",
            (node as JSCallExpression).arguments.first().text
        )

    override fun getShortName() = "JSDeepBugsIncorrectArgMath"
}
