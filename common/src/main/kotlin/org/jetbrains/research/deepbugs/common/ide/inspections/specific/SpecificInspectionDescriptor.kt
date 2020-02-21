package org.jetbrains.research.deepbugs.common.ide.inspections.specific

import com.intellij.psi.PsiElement

data class SpecificInspectionDescriptor(
    val shouldProcess: (PsiElement) -> Boolean
)

