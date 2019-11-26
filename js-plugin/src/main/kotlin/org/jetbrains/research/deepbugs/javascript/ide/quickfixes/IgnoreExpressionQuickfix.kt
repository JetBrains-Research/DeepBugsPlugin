package org.jetbrains.research.deepbugs.javascript.ide.quickfixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class IgnoreExpressionQuickfix(private val expr: String) : LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        JSDeepBugsConfig.ignoreExpression(expr)
    }

    override fun getFamilyName(): String {
        return JSResourceBundle.message("deepbugs.javascript.quickfixes.family")
    }

    override fun getName(): String {
        return JSResourceBundle.message("deepbugs.javascript.ignore.quickfix")
    }
}
