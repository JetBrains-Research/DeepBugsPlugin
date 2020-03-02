package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInspection.ProblemDescriptor
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.deepbugs.common.datatypes.Call
import org.jetbrains.research.deepbugs.common.ide.problem.BugDescriptor
import org.jetbrains.research.deepbugs.common.ide.quickfixes.FlipFunctionArgumentsQuickFix
import org.jetbrains.research.deepbugs.common.model.CommonModelStorage
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.python.ide.quickfixes.PyIgnoreExpressionQuickFix
import org.jetbrains.research.keras.runner.nn.model.sequential.Perceptron

class PyDeepBugsSwappedArgsInspection : PyDeepBugsCallExprInspection(2, 0.8f) {
    override val model: Perceptron?
        get() = CommonModelStorage.common.swappedArgsModel

    override fun skip(node: PyCallExpression): Boolean = node.arguments.size != requiredArgsNum

    override fun createProblemDescriptor(node: PyCallExpression, data: Call, onTheFly: Boolean): ProblemDescriptor =
        BugDescriptor(node, createTooltip(node), onTheFly, listOf(
            PyIgnoreExpressionQuickFix(data, node.text),
            FlipFunctionArgumentsQuickFix(PyResourceBundle.message("deepbugs.python.flip.args.family"))
        ))

    override fun createTooltip(node: PyCallExpression, vararg params: String): String =
        PyResourceBundle.message("deepbugs.python.swapped.args.inspection.warning")

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.swapped.args.inspection.display")
    override fun getShortName(): String = "PyDeepBugsSwappedArgs"
}
