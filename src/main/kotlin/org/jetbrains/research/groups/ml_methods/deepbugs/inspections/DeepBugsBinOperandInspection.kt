package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

class DeepBugsBinOperandInspection : DeepBugsBinExprInspection() {
    override val keyMessage: String = "binary.operand.inspection.warning"

    override fun getModel() = ModelsManager.binOperandModel
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPluginBundle.message("binary.operand.inspection.display")
    override fun getShortName(): String = DeepBugsPluginBundle.message("bin.operand.inspection.short.name")
}