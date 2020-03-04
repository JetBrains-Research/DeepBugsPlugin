package org.jetbrains.research.deepbugs.common.ide.quickfixes

import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.DeepBugsCounterCollector

abstract class FlipFunctionArgumentsQuickFix<T : PsiElement> : LocalQuickFix, PriorityAction {
    override fun getName(): String = CommonResourceBundle.message("deepbugs.flip.arguments.quickfix")

    override fun getPriority(): PriorityAction.Priority = PriorityAction.Priority.HIGH

    protected abstract fun T.toArguments(): Pair<PsiElement, PsiElement>

    @Suppress("UNCHECKED_CAST")
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement as? T ?: return

        val (arg0, arg1) = element.toArguments()

        arg0.replace(arg1)
        arg1.replace(arg0)

        DeepBugsCounterCollector.quickFixApplied(project, "flip.arguments", cancelled = false)
    }
}
