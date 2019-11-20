package org.jetbrains.research.deepbugs.javascript.inspections

import org.jetbrains.research.deepbugs.javascript.JSModelManager
import org.jetbrains.research.deepbugs.javascript.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.javascript.swapped.args.inspection.warning"

    override fun getModel() = JSModelManager.storage?.swappedArgsModel
    override fun getThreshold() = JSDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.display")
    override fun getShortName() = "JSCallInspection"
}
