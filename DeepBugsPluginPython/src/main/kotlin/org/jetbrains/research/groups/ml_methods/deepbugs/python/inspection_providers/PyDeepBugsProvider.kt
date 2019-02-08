package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspection_providers

import com.intellij.codeInspection.InspectionToolProvider
import com.intellij.openapi.application.PathManager
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsBinOperatorInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsSwappedArgsInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.PlatformUtils
import java.nio.file.Paths

class PyDeepBugsProvider : InspectionToolProvider {

    init {
        PlatformUtils.loadLibs()
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(PyDeepBugsBinOperatorInspection::class.java, PyDeepBugsBinOperandInspection::class.java,
                PyDeepBugsSwappedArgsInspection::class.java)
    }
}