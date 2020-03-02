package org.jetbrains.research.deepbugs.javascript.ide.inspections.common

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.lang.javascript.psi.JSCallExpression
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.common.ide.inspections.DeepBugsInspectionManager
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

open class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection(2) {
    override val model: Perceptron?
        get() = CommonModelStorage.common.swappedArgsModel

    override fun skip(node: JSCallExpression): Boolean {
        if (node.arguments.size != requiredArgumentsNum) return true
        return DeepBugsInspectionManager.isSpecific(node)
    }

    override fun createProblemDescriptor(node: JSCallExpression, data: Call): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), myOnTheFly, listOf(
            JSIgnoreExpressionQuickFix(data, node.text),
            FlipFunctionArgumentsQuickFix(JSResourceBundle.message("deepbugs.javascript.flip.args.family"))
        ))

    override fun createTooltip(node: JSCallExpression, vararg params: String): String =
        JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.warning")

    override fun getShortName() = "JSDeepBugsSwappedArgs"
}
