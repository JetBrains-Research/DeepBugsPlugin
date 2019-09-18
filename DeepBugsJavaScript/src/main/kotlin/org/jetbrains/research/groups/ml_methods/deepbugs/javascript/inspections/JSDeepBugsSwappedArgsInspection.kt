package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.base.JSDeepBugsCallExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.base.models
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle

class JSDeepBugsSwappedArgsInspection : JSDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.javascript.swapped.args.inspection.warning"

    override fun getModel() = models.swappedArgsModel
    override fun getThreshold() = JSDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold

    override fun getDisplayName() = DeepBugsJSBundle.message("deepbugs.javascript.swapped.args.inspection.display")
    override fun getShortName() = "JSCallInspection"
}