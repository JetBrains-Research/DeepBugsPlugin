package org.jetbrains.research.deepbugs.python.inspections

import org.jetbrains.research.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.inspections.base.models
import org.jetbrains.research.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.python.utils.DeepBugsPythonBundle

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operator.inspection.warning"

    override fun getModel() = models.storage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("deepbugs.python.bin.operator.inspection.display")
    override fun getShortName() = "PyOperatorInspection"
}
