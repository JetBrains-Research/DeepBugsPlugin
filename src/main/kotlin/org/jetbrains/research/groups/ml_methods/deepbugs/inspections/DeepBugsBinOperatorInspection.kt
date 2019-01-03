package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

class DeepBugsBinOperatorInspection : DeepBugsBinExprInspection() {
    override val keyMessage: String = "binary.operator.inspection.warning"

    override fun getModel() = ModelsManager.binOperatorModel
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPluginBundle.message("binary.operator.inspection.display")
    override fun getShortName(): String = "DeepBugsBinOperator"
}