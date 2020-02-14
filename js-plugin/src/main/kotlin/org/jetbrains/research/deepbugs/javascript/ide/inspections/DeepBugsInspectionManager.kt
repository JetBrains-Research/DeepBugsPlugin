package org.jetbrains.research.deepbugs.javascript.ide.inspections

import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.javascript.ide.inspections.specific.SpecificInspectionDescriptor

object DeepBugsInspectionManager {
    private val specificDescriptors: HashSet<SpecificInspectionDescriptor> = HashSet()

    fun register(descriptor: SpecificInspectionDescriptor) = specificDescriptors.add(descriptor)

    fun isSpecific(element: PsiElement) = specificDescriptors.any { it.shouldProcess(element) }
}
