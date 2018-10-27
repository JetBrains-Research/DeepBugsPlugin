package org.jetbrains.research.groups.ml_methods.deepbugs.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfig
import org.tensorflow.SavedModelBundle
import org.tensorflow.Session
import java.nio.file.Files
import java.nio.file.Paths

class DeepBugsBinOperatorInspection : DeepBugsBinExprInspection() {
    override val model: SavedModelBundle? = if (Files.exists(Paths.get("$root/models/binOperatorDetectionModel")))
                                                SavedModelBundle.load("$root/models/binOperatorDetectionModel", "serve")
                                            else null
    override val modelSession: Session? = model?.session()
    override val keyMessage: String = "binary.operator.inspection.warning"
    override fun getThreshold(): Float = DeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = "DeepBugs: Possibly incorrect binary operator"
    override fun getShortName(): String = "DeepBugsBinOperator"
}