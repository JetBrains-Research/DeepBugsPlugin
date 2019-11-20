package org.jetbrains.research.deepbugs.javascript.inspections.providers

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.deepbugs.javascript.inspections.*
import org.jetbrains.research.deepbugs.services.utils.platform.PlatformManager

class JSDeepBugsProvider : InspectionToolProvider {
    init {
        PlatformManager.checkPlatformAndDependencies<JSDeepBugsProvider>()
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(JSDeepBugsBinOperatorInspection::class.java, JSDeepBugsBinOperandInspection::class.java,
            JSDeepBugsSwappedArgsInspection::class.java)
    }
}
