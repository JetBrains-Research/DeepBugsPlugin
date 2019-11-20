package org.jetbrains.research.deepbugs.javascript.inspections

import org.jetbrains.research.deepbugs.javascript.JSModelManager
import org.jetbrains.research.deepbugs.javascript.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.javascript.JSResourceBundle

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.javascript.binary.operator.inspection.warning"

    override fun getModel() = JSModelManager.storage?.binOperatorModel
    override fun getThreshold() = JSDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = JSResourceBundle.message("deepbugs.javascript.binary.operator.inspection.display")
    override fun getShortName() = "JSOperatorInspection"
}
