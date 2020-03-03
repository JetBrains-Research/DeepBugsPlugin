package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.math

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.lang.javascript.psi.JSCallExpression
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.deepbugs.javascript.model.specific.JSSpecificModel
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsSwappedArgsMathInspection : JSDeepBugsMathCallExprInspection(requiredArgsNum = 2) {
    override val model: Perceptron?
        get() = JSSpecificModel.math.swappedArgsModel

    override val libsToConsider: Set<String> = setOf("mathjs")
    override val ignore: Set<String> = setOf("min", "max")

    override fun createProblemDescriptor(node: JSCallExpression, data: Call): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), myOnTheFly, listOf(
            JSIgnoreExpressionQuickFix(data, node.text),
            FlipFunctionArgumentsQuickFix(JSResourceBundle.message("deepbugs.javascript.flip.args.family"))
        ))

    override fun createTooltip(node: JSCallExpression, vararg params: String): String =
        JSResourceBundle.message("deepbugs.javascript.math.swapped.args.inspection.warning")

    override fun getShortName() = "JSDeepBugsSwappedArgsMath"
}
