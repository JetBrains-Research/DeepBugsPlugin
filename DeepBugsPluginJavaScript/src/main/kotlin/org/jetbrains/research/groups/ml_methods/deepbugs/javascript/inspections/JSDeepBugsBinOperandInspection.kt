package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.models

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage: String = "javascript.binary.operand.inspection.warning"

    override fun getModel() = models.binOperandModel
    override fun getThreshold(): Float = JSDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsJSBundle.message("javascript.binary.operand.inspection.display")
    override fun getShortName(): String = DeepBugsJSBundle.message("javascript.bin.operand.inspection.short.name")

}