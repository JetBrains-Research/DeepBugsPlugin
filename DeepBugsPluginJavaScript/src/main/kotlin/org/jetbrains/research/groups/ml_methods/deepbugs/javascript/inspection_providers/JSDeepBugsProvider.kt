package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspection_providers

import com.intellij.codeInspection.InspectionToolProvider
import com.intellij.openapi.application.PathManager
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.TensorFlowPlatformUtils
import java.nio.file.Paths

class JSDeepBugsProvider : InspectionToolProvider {

    init {
        TensorFlowPlatformUtils.loadLibs(Paths.get(PathManager.getPluginsPath(), DeepBugsJSBundle.message("plugin.name"), "bundlers").toString())
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        //TODO: return array of implemented inspections
        return arrayOf()
    }
}