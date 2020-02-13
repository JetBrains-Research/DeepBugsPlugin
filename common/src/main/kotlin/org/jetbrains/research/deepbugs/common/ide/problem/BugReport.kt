package org.jetbrains.research.deepbugs.common.ide.problem

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.psi.PsiElement

data class BugReport(
    val element: PsiElement,
    val description: String,
    val fixes: List<LocalQuickFix> = emptyList()
)
