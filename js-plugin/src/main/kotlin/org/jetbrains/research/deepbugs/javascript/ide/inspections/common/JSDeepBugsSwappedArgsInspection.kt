package org.jetbrains.research.deepbugs.javascript.ide.inspections.common

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.psi.NavigatablePsiElement
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.DeepBugsInspectionManager
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

open class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection() {
    override val requiredArgumentsNum: Int? = 2

    override val model: Perceptron?
        get() = CommonModelStorage.common.swappedArgsModel

    override fun skip(node: NavigatablePsiElement): Boolean = (node as JSCallExpression).methodExpression?.let {
        node.arguments.size != requiredArgumentsNum || DeepBugsInspectionManager.isSpecific(it)
    } ?: true

    override fun createProblemDescriptor(node: NavigatablePsiElement, data: DataType): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), listOf(
            JSIgnoreExpressionQuickFix(data, node.text),
            FlipFunctionArgumentsQuickFix(JSResourceBundle.message("deepbugs.javascript.flip.args.family"))
        ))

    override fun createTooltip(node: NavigatablePsiElement, vararg params: Any): String =
        JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.warning")

    override fun getShortName() = "JSDeepBugsSwappedArgs"
}