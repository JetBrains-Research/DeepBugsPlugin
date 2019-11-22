package org.jetbrains.research.deepbugs.javascript.inspections

import com.intellij.application.subscribe
import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle
import org.jetbrains.research.deepbugs.javascript.JSModelManager
import org.jetbrains.research.deepbugs.javascript.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection(), DeepBugsToolLifecycle {
    init {
        DeepBugsToolLifecycle.targetTopic.subscribe(ApplicationManager.getApplication(), this)
    }
    override val keyMessage = "deepbugs.javascript.binary.operator.inspection.warning"

    override fun getModel() = JSModelManager.storage?.binOperatorModel
    override fun getThreshold() = JSDeepBugsInspectionConfig.getConfig().curBinOperatorThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.binary.operator.inspection.display")
    override fun getShortName() = "JSOperatorInspection"
}
