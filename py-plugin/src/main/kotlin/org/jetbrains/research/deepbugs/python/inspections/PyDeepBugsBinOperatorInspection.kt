package org.jetbrains.research.deepbugs.python.inspections

import org.jetbrains.research.deepbugs.python.PyModelManager
import org.jetbrains.research.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operator.inspection.warning"

    override fun getModel() = PyModelManager.storage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curBinOperatorThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.bin.operator.inspection.display")
    override fun getShortName() = "PyOperatorInspection"
}
