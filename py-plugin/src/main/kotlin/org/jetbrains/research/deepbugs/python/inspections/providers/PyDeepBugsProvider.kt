package org.jetbrains.research.deepbugs.python.inspections.providers

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.deepbugs.python.inspections.*
import org.jetbrains.research.deepbugs.common.utils.platform.PlatformManager

class PyDeepBugsProvider : InspectionToolProvider {
    init {
        PlatformManager.checkPlatformAndDependencies<PyDeepBugsProvider>()
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(PyDeepBugsBinOperatorInspection::class.java, PyDeepBugsBinOperandInspection::class.java,
            PyDeepBugsSwappedArgsInspection::class.java)
    }
}
