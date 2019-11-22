package org.jetbrains.research.deepbugs.javascript.inspections

import com.intellij.application.subscribe
import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle
import org.jetbrains.research.deepbugs.javascript.JSModelManager
import org.jetbrains.research.deepbugs.javascript.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection(), DeepBugsToolLifecycle {
    init {
        DeepBugsToolLifecycle.targetTopic.subscribe(ApplicationManager.getApplication(), this)
    }
    override val keyMessage = "deepbugs.javascript.binary.operand.inspection.warning"

    override fun getModel() = JSModelManager.storage?.binOperandModel
    override fun getThreshold() = JSDeepBugsInspectionConfig.getConfig().curBinOperandThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.binary.operand.inspection.display")
    override fun getShortName() = "JSOperandInspection"

}
