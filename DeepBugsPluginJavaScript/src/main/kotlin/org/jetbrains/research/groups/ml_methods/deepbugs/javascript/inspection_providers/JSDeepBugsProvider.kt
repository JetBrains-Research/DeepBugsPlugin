package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspection_providers

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.JSDeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.JSDeepBugsBinOperatorInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.JSDeepBugsSwappedArgsInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.PlatformUtils

class JSDeepBugsProvider : InspectionToolProvider {

    init {
        PlatformUtils.loadLibs()
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(JSDeepBugsBinOperatorInspection::class.java, JSDeepBugsBinOperandInspection::class.java,
                JSDeepBugsSwappedArgsInspection::class.java)
    }
}