package org.jetbrains.research.deepbugs.javascript.ide.inspections

import org.jetbrains.research.deepbugs.javascript.*
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection

class JSDeepBugsBinOperandInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.javascript.binary.operand.inspection.warning"

    override fun getModel() = JSModelManager.storage?.binOperandModel
    override fun getThreshold() = JSDeepBugsConfig.get().binOperandThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.binary.operand.inspection.display")
    override fun getShortName() = "JSOperandInspection"
}
