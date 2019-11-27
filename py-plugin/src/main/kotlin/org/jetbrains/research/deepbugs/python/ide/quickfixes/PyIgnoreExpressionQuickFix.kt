package org.jetbrains.research.deepbugs.python.ide.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.command.undo.BasicUndoableAction
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import javax.swing.Icon

class PyIgnoreExpressionQuickFix(private val expr: String) : LocalQuickFix, Iconable {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val ignore = object : BasicUndoableAction(descriptor.psiElement?.containingFile?.virtualFile) {
            override fun redo() = PyDeepBugsConfig.ignoreExpression(expr)
            override fun undo() = PyDeepBugsConfig.considerExpression(expr)
        }
        ignore.redo()
        UndoManager.getInstance(project).undoableActionPerformed(ignore)
    }

    override fun getIcon(flags: Int): Icon = AllIcons.Actions.Cancel

    override fun getFamilyName(): String = PyResourceBundle.message("deepbugs.python.display")
    override fun getName(): String = PyResourceBundle.message("deepbugs.python.ignore.quickfix", expr)
}
