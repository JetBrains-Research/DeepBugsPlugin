package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.deepbugs.javascript.model.JSModelStorage
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsSwappedArgsMathInspection : JSDeepBugsMathCallExprInspection() {
    override val requiredArgumentsNum: Int? = 2
    override val model: Perceptron?
        get() = JSModelStorage.specific.math.swappedArgsModel

    override fun createProblemDescriptor(node: NavigatablePsiElement, data: DataType): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), listOf(
            JSIgnoreExpressionQuickFix(data, node.text),
            FlipFunctionArgumentsQuickFix(JSResourceBundle.message("deepbugs.javascript.flip.args.family"))
        ))

    override fun createTooltip(node: NavigatablePsiElement, vararg params: Any): String =
        JSResourceBundle.message("deepbugs.javascript.math.swapped.args.inspection.warning")

    override fun getShortName() = "JSDeepBugsSwappedArgsMath"

}
