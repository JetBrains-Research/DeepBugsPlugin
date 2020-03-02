package org.jetbrains.research.deepbugs.common.ide.problem

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptorBase
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray

class BugDescriptor(element: PsiElement, description: String, onTheFly: Boolean, fixes: List<LocalQuickFix> = emptyList()) : ProblemDescriptorBase(
    element,
    element,
    description,
    fixes.toArray(LocalQuickFix.EMPTY_ARRAY),
    ProblemHighlightType.GENERIC_ERROR,
    false,
    null,
    true,
    onTheFly
)
