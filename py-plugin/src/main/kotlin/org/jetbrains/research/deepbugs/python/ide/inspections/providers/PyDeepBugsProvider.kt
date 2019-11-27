package org.jetbrains.research.deepbugs.python.ide.inspections.providers

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.deepbugs.python.ide.inspections.*

class PyDeepBugsProvider : InspectionToolProvider {
    override fun getInspectionClasses(): Array<Class<*>> = arrayOf(
        PyDeepBugsBinOperatorInspection::class.java,
        PyDeepBugsBinOperandInspection::class.java,
        PyDeepBugsSwappedArgsInspection::class.java
    )
}
