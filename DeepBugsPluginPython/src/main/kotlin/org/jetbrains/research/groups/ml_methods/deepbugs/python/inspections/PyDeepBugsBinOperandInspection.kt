package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage: String = "bin.operand.inspection.warning"

    override fun getModel() = DeepBugsPythonService.models.binOperandModel
    override fun getThreshold(): Float = PyDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("bin.operand.inspection.display")
    override fun getShortName(): String = DeepBugsPythonBundle.message("bin.operand.inspection.short.name")
}