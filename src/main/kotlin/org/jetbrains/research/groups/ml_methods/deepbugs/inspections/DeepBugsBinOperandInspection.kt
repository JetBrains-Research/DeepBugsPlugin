package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session

class DeepBugsBinOperandInspection : DeepBugsBinExprInspection() {
    override val model: SavedModelBundle = SavedModelBundle.load("$root/models/binOperandDetectionModel", "serve")
    override val modelSession: Session = model.session()
    override val keyMessage: String = "binary.operand.inspection.warning"
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = "DeepBugs: Possibly incorrect binary operand"
    override fun getShortName(): String = "DeepBugsBinOperand"
}