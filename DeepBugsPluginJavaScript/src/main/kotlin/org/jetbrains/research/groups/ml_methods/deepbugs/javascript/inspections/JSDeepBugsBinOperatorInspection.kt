package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings.JSDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils.DeepBugsJSBundle

class JSDeepBugsBinOperatorInspection : JSDeepBugsBinExprInspection() {
    override val keyMessage: String = "binary.operator.inspection.warning"

    override fun getModel() = ModelsManager.binOperatorModel
    override fun getThreshold(): Float = JSDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsJSBundle.message("binary.operator.inspection.display")
    override fun getShortName(): String = DeepBugsJSBundle.message("bin.operator.inspection.short.name")
}