package org.jetbrains.research.deepbugs.javascript.inspections

import com.intellij.application.subscribe
import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle
import org.jetbrains.research.deepbugs.javascript.JSModelManager
import org.jetbrains.research.deepbugs.javascript.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection(), DeepBugsToolLifecycle {
    init {
        DeepBugsToolLifecycle.targetTopic.subscribe(ApplicationManager.getApplication(), this)
    }
    override val keyMessage = "deepbugs.javascript.swapped.args.inspection.warning"

    override fun getModel() = JSModelManager.storage?.swappedArgsModel
    override fun getThreshold() = JSDeepBugsInspectionConfig.getConfig().curSwappedArgsThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.display")
    override fun getShortName() = "JSCallInspection"
}
