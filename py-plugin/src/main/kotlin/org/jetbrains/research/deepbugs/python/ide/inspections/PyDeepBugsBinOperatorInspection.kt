package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInspection.ProblemDescriptor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.datatypes.BinOp
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix
import org.jetbrains.research.deepbugs.common.ide.quickfixes.ReplaceBinOperatorQuickFix.Companion.toLookups
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.extraction.extractOperatorText
import org.jetbrains.research.deepbugs.python.extraction.findOperatorTextRange
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection(0.85f) {
    override val model: Perceptron?
        get() = CommonModelStorage.common.binOperatorModel

    override fun createProblemDescriptor(node: PyBinaryExpression, data: DataType): ProblemDescriptor {
        val textRange = node.findOperatorTextRange()!!
        val replaceQuickFix = ReplaceBinOperatorQuickFix(data as BinOp, textRange, PyDeepBugsConfig.get().quickFixesThreshold,
            PyResourceBundle.message("deepbugs.python.replace.operator.family")).takeIf { it.isAvailable() }
        return BugDescriptor(
            node,
            createTooltip(node, *(replaceQuickFix.toLookups())),
            listOfNotNull(PyIgnoreExpressionQuickFix(data, node.text), replaceQuickFix)
        )
    }

    override fun createTooltip(node: PyBinaryExpression, vararg params: String): String {
        val operatorText = node.extractOperatorText() ?: ""
        return params.singleOrNull()?.let {
            PyResourceBundle.message("deepbugs.python.binary.operator.inspection.warning.single", it, operatorText)
        } ?: PyResourceBundle.message("deepbugs.python.binary.operator.inspection.warning", operatorText)
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operator.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperator"
}
