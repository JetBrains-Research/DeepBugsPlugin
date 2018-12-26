package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.ModelsHolder

class DeepBugsBinOperandInspection : DeepBugsBinExprInspection() {

    override val keyMessage: String = "binary.operand.inspection.warning"

    override fun getModel() = ModelsHolder.binOperandModelSession
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPluginBundle.message("binary.operand.inspection.display")
    override fun getShortName(): String = "DeepBugsBinOperand"
}