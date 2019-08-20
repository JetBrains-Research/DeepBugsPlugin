package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService.models

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {

    override val keyMessage: String = "deepbugs.python.bin.operator.inspection.warning"

    override fun getModel() = models.binOperatorModel
    override fun getThreshold(): Float = PyDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("deepbugs.python.bin.operator.inspection.display")
    override fun getShortName(): String = "PyOperatorInspection"
}