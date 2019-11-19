package org.jetbrains.research.deepbugs.javascript.inspections

import org.jetbrains.research.deepbugs.javascript.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.javascript.inspections.base.models
import org.jetbrains.research.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.javascript.utils.DeepBugsJSBundle

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.javascript.binary.operand.inspection.warning"

    override fun getModel() = models.getBinOperandModel()
    override fun getThreshold() = JSDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsJSBundle.message("deepbugs.javascript.binary.operand.inspection.display")
    override fun getShortName() = "JSOperandInspection"

}
