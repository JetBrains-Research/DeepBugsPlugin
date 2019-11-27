package org.jetbrains.research.deepbugs.javascript.ide.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.command.undo.BasicUndoableAction
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import org.jetbrains.research.deepbugs.common.datatypes.DataType
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle
import javax.swing.Icon

class JSIgnoreExpressionQuickFix(private val expr: DataType, private val displayText: String) : LocalQuickFix, Iconable {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val ignore = object : BasicUndoableAction(descriptor.psiElement?.containingFile?.virtualFile) {
            override fun redo() = JSDeepBugsConfig.ignoreExpression(expr)
            override fun undo() = JSDeepBugsConfig.considerExpression(expr)
        }
        ignore.redo()
        UndoManager.getInstance(project).undoableActionPerformed(ignore)
    }

    override fun getIcon(flags: Int): Icon = AllIcons.Actions.Cancel

    override fun getFamilyName(): String = JSResourceBundle.message("deepbugs.javascript.display")
    override fun getName(): String = JSResourceBundle.message("deepbugs.javascript.ignore.quickfix", displayText)
}
