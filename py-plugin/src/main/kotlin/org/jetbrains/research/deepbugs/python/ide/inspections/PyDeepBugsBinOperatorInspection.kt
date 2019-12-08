package org.jetbrains.research.deepbugs.python.ide.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.psi.PyBinaryExpression
import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.keras.runner.nn.model.sequential.Perceptron
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.datatypes.PyBinOp
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val model: Perceptron?
        get() = ModelManager.storage.binOperatorModel
    override val threshold: Float
        get() = PyDeepBugsConfig.get().binOperatorThreshold

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor = object : PyDeepBugsBinOpVisitor(holder, session) {
        override fun msg(node: NavigatablePsiElement): String = (node as PyBinaryExpression).let {
            PyResourceBundle.message(
                "deepbugs.python.binary.operator.inspection.warning",
                PyBinOp.extractOperatorText(node) ?: ""
            )
        }
    }

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.binary.operator.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperator"
}
