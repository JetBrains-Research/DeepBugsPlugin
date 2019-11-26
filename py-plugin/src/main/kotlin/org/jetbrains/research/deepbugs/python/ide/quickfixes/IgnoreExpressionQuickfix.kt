package org.jetbrains.research.deepbugs.python.ide.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class IgnoreExpressionQuickfix(private val expr: String) : LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        PyDeepBugsConfig.ignoreExpression(expr)
    }

    override fun getFamilyName(): String {
        return PyResourceBundle.message("deepbugs.python.quickfixes.family")
    }

    override fun getName(): String {
        return PyResourceBundle.message("deepbugs.python.ignore.quickfix")
    }
}
