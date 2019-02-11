package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage: String = "bin.operator.inspection.warning"

    override fun getModel() = DeepBugsPythonService.models.binOperatorModel
    override fun getThreshold(): Float = PyDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("bin.operator.inspection.display")
    override fun getShortName(): String = DeepBugsPythonBundle.message("bin.operator.inspection.short.name")
}