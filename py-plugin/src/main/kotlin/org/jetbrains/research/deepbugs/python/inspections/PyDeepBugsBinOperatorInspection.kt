package org.jetbrains.research.deepbugs.python.inspections

import com.intellij.application.subscribe
import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle
import org.jetbrains.research.deepbugs.python.PyModelManager
import org.jetbrains.research.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection(), DeepBugsToolLifecycle {
    init {
        DeepBugsToolLifecycle.targetTopic.subscribe(ApplicationManager.getApplication(), this)
    }
    override val keyMessage = "deepbugs.python.bin.operator.inspection.warning"

    override fun getModel() = PyModelManager.storage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getConfig().curBinOperatorThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.bin.operator.inspection.display")
    override fun getShortName() = "PyOperatorInspection"
}
