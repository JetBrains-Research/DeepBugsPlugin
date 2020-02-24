package org.jetbrains.research.deepbugs.common.ide.inspections

import com.intellij.psi.PsiElement
import org.jetbrains.research.deepbugs.common.ide.inspections.specific.SpecificInspectionDescriptor
import java.util.concurrent.ConcurrentHashMap

object DeepBugsInspectionManager {
    private val specificDescriptors = ConcurrentHashMap.newKeySet<SpecificInspectionDescriptor>()

    fun register(descriptor: SpecificInspectionDescriptor) = specificDescriptors.add(descriptor)

    fun isSpecific(element: PsiElement) = specificDescriptors.any { it.shouldProcess(element) }
}
