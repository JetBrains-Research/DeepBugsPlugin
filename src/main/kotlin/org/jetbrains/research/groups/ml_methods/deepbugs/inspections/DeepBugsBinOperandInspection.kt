package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsHolder
import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

class DeepBugsBinOperandInspection : DeepBugsBinExprInspection() {
    override val keyMessage: String = "binary.operand.inspection.warning"

    override fun getModel() = ModelsHolder.binOperandModel
    override fun getThreshold(): Double = DeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPluginBundle.message("binary.operand.inspection.display")
    override fun getShortName(): String = "DeepBugsBinOperand"
}