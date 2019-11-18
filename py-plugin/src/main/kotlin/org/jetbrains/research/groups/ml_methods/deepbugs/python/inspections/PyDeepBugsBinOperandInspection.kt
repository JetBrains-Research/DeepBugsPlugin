package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.models
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operand.inspection.warning"

    override fun getModel() = models.binOperandModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("deepbugs.python.bin.operand.inspection.display")
    override fun getShortName() = "PyOperandInspection"
}