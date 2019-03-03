package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.models

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage: String = "python.bin.operand.inspection.warning"

    override fun getModel() = models.binOperandModel
    override fun getThreshold(): Float = PyDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("python.bin.operand.inspection.display")
    override fun getShortName(): String = DeepBugsPythonBundle.message("python.bin.operand.inspection.short.name")
}