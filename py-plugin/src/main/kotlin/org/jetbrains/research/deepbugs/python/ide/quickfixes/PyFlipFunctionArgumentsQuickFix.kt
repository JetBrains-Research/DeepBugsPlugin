package org.jetbrains.research.deepbugs.python.ide.quickfixes

import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.jetbrains.python.psi.PyCallExpression
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.DeepBugsCounterCollector
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyFlipFunctionArgumentsQuickFix : LocalQuickFix, PriorityAction {
    override fun getName(): String = CommonResourceBundle.message("deepbugs.flip.arguments.quickfix")
    override fun getFamilyName(): String = PyResourceBundle.message("deepbugs.python.flip.args.family")

    override fun getPriority(): PriorityAction.Priority = PriorityAction.Priority.HIGH

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement as? PyCallExpression ?: return

        val arg0 = element.arguments[0]
        val arg1 = element.arguments[1]

        element.arguments[0].replace(arg1)
        element.arguments[1].replace(arg0)

        DeepBugsCounterCollector.quickFixApplied(project, "flip.arguments", cancelled = false)
    }
}
