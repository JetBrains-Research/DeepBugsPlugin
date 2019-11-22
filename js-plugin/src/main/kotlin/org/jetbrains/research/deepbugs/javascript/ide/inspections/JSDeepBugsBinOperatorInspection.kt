package org.jetbrains.research.deepbugs.javascript.ide.inspections

import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.javascript.*
import org.jetbrains.research.deepbugs.javascript.ide.inspections.base.JSDeepBugsBinExprInspection

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.javascript.binary.operator.inspection.warning"

    override fun getModel() = ModelManager.storage?.binOperatorModel
    override fun getThreshold() = JSDeepBugsConfig.get().binOperatorThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.binary.operator.inspection.display")
    override fun getShortName() = "JSOperatorInspection"
}
