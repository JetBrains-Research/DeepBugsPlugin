package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections.base.JSDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.models

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage: String = "deepbugs.javascript.binary.operator.inspection.warning"

    override fun getModel() = models.binOperatorModel
    override fun getThreshold(): Float = JSDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsJSBundle.message("deepbugs.javascript.binary.operator.inspection.display")
    override fun getShortName(): String = "JSDeepBugsBinOperator"
}