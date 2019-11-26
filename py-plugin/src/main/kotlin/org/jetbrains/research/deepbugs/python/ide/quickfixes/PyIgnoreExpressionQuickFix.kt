package org.jetbrains.research.deepbugs.python.ide.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.command.undo.BasicUndoableAction
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyIgnoreExpressionQuickFix(private val expr: String) : LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val ignore = object : BasicUndoableAction(descriptor.psiElement?.containingFile?.virtualFile) {
            override fun redo() = PyDeepBugsConfig.ignoreExpression(expr)
            override fun undo() = PyDeepBugsConfig.considerExpression(expr)
        }
        ignore.redo()
        UndoManager.getInstance(project).undoableActionPerformed(ignore)
    }

    override fun getFamilyName(): String {
        return PyResourceBundle.message("deepbugs.python.quickfixes.family")
    }

    override fun getName(): String {
        return PyResourceBundle.message("deepbugs.python.ignore.quickfix")
    }
}
