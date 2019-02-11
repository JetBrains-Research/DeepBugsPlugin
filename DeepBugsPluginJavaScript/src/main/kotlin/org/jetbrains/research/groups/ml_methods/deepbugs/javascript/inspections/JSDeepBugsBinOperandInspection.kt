package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSService

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage: String = "binary.operand.inspection.warning"

    override fun getModel() = DeepBugsJSService.models.binOperandModel
    override fun getThreshold(): Float = JSDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsJSBundle.message("binary.operand.inspection.display")
    override fun getShortName(): String = DeepBugsJSBundle.message("bin.operand.inspection.short.name")
}