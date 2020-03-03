package org.jetbrains.research.deepbugs.common.ide.quickfixes

import com.intellij.codeInsight.intention.PriorityAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actions.FlipCommaIntention
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.DeepBugsCounterCollector

class FlipFunctionArgumentsQuickFix(private val displayName: String) : LocalQuickFix, PriorityAction {
    override fun getName(): String = CommonResourceBundle.message("deepbugs.flip.arguments.quickfix")
    override fun getFamilyName(): String = displayName

    override fun getPriority(): PriorityAction.Priority = PriorityAction.Priority.HIGH

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        DataManager.getInstance().dataContextFromFocusAsync.onSuccess { context ->
            val editor: Editor = CommonDataKeys.EDITOR.getData(context) ?: return@onSuccess
            val file: PsiFile = CommonDataKeys.PSI_FILE.getData(context) ?: return@onSuccess

            FlipCommaIntention().invoke(project, editor, file)
            DeepBugsCounterCollector.quickFixApplied(project, "flip.arguments", cancelled = false)
        }
    }
}
