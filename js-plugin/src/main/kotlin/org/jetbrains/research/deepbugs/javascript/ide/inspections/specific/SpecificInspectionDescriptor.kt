package org.jetbrains.research.deepbugs.javascript.ide.inspections.specific

import com.intellij.psi.PsiElement

interface SpecificInspectionDescriptor {
    fun shouldProcess(element: PsiElement): Boolean
}

