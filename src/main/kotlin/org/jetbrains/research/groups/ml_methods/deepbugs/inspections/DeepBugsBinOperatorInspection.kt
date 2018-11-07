package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Paths

class DeepBugsBinOperatorInspection : DeepBugsBinExprInspection() {

    override val model: SavedModelBundle = SavedModelBundle.load(Paths.get(root, "models", "binOperatorDetectionModel").toString(), "serve")
    override val modelSession: Session = model.session()
    override val keyMessage: String = "binary.operator.inspection.warning"
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPluginBundle.message("binary.operator.inspection.display")
    override fun getShortName(): String = "DeepBugsBinOperator"
}