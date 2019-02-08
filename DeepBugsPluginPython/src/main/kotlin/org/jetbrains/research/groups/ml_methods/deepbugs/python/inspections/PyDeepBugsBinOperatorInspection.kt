package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import java.util.*

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val uuid = UUID.randomUUID()
    override val keyMessage: String = "binary.operator.inspection.warning"
    override val bugName = "IncorrectBinaryOperator"

    override fun getModel() = ModelsManager.binOperatorModel
    override fun getThreshold(): Float = PyDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("binary.operator.inspection.display")
    override fun getShortName(): String = DeepBugsPythonBundle.message("bin.operator.inspection.short.name")
}