package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspection_providers

import com.intellij.codeInspection.InspectionToolProvider

import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsBinOperatorInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsSwappedArgsInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.PlatformManager

class PyDeepBugsProvider : InspectionToolProvider {
    init {
        PlatformManager.checkPlatformAndDependencies(PyDeepBugsProvider::class.java)
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(PyDeepBugsBinOperatorInspection::class.java, PyDeepBugsBinOperandInspection::class.java,
            PyDeepBugsSwappedArgsInspection::class.java)
    }
}