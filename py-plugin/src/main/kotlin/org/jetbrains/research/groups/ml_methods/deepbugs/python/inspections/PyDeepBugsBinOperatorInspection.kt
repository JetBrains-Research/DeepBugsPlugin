package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.models
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operator.inspection.warning"

    override fun getModel() = models.binOperatorModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("deepbugs.python.bin.operator.inspection.display")
    override fun getShortName() = "PyOperatorInspection"
}