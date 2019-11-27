package org.jetbrains.research.deepbugs.javascript.ide.inspections.providers

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.deepbugs.javascript.ide.inspections.*

class JSDeepBugsProvider : InspectionToolProvider {
    override fun getInspectionClasses(): Array<Class<*>> = arrayOf(
        JSDeepBugsBinOperatorInspection::class.java,
        JSDeepBugsBinOperandInspection::class.java,
        JSDeepBugsSwappedArgsInspection::class.java
    )
}
