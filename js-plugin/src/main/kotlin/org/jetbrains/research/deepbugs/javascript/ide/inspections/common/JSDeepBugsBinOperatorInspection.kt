package org.jetbrains.research.deepbugs.javascript.ide.inspections.common

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.lang.javascript.psi.JSBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix.Companion.toLookups
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.JSIgnoreExpressionQuickFix
import org.jetbrains.research.deepbugs.javascript.ide.quickfixes.utils.operators
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = CommonModelStorage.common.binOperatorModel

    override fun createProblemDescriptor(node: JSBinaryExpression, data: BinOp): ProblemDescriptor {
        val textRange = node.operationNode!!.textRange
        val replaceQuickFix = ReplaceBinOperatorQuickFix(data, textRange, JSDeepBugsConfig.get().quickFixesThreshold,
            JSResourceBundle.message("deepbugs.javascript.replace.operator.family")) { operators[it] ?: "" }.takeIf { it.isAvailable() }
        return BugDescriptor(
            node,
            createTooltip(node, *(replaceQuickFix.toLookups())),
            myOnTheFly,
            listOfNotNull(JSIgnoreExpressionQuickFix(data, node.text), replaceQuickFix)
        )
    }

    override fun createTooltip(node: JSBinaryExpression, vararg params: String): String {
        val operatorText = node.operationNode?.text ?: ""
        return params.singleOrNull()?.let {
            JSResourceBundle.message("deepbugs.javascript.binary.operator.inspection.warning.single", it, operatorText)
        } ?: JSResourceBundle.message("deepbugs.javascript.binary.operator.inspection.warning", operatorText)
    }

    override fun getShortName() = "JSDeepBugsBinOperator"
}
