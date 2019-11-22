package org.jetbrains.research.deepbugs.javascript.ide.inspections

import org.jetbrains.research.deepbugs.javascript.*
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsCallExprInspection

class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.javascript.swapped.args.inspection.warning"

    override fun getModel() = JSModelManager.storage?.swappedArgsModel
    override fun getThreshold() = JSDeepBugsConfig.get().swappedArgsThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.swapped.args.inspection.display")
    override fun getShortName() = "JSCallInspection"
}
