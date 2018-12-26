package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.ModelsHolder

class DeepBugsBinOperatorInspection : DeepBugsBinExprInspection() {

    override val keyMessage: String = "binary.operator.inspection.warning"

    override fun getModel() = ModelsHolder.binOperatorModelSession
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPluginBundle.message("binary.operator.inspection.display")
    override fun getShortName(): String = "DeepBugsBinOperator"
}