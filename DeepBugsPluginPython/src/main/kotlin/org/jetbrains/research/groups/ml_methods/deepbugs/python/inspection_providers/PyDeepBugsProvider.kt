package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspection_providers

import com.intellij.codeInspection.InspectionToolProvider
import com.intellij.openapi.application.PathManager
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsBinOperatorInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.PyDeepBugsSwappedArgsInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.TensorFlowPlatformUtils
import java.nio.file.Paths

class PyDeepBugsProvider : InspectionToolProvider {

    init {
        TensorFlowPlatformUtils.loadLibs(Paths.get(PathManager.getPluginsPath(), "DeepBugsPython").toString())
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(PyDeepBugsBinOperatorInspection::class.java, PyDeepBugsBinOperandInspection::class.java,
                PyDeepBugsSwappedArgsInspection::class.java)
    }
}