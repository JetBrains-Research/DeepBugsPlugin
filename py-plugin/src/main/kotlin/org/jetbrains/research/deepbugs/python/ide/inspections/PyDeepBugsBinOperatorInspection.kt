package org.jetbrains.research.deepbugs.python.ide.inspections

import org.jetbrains.research.deepbugs.python.*
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection

class PyDeepBugsBinOperatorInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operator.inspection.warning"

    override fun getModel() = PyModelManager.storage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsConfig.get().binOperatorThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.bin.operator.inspection.display")
    override fun getShortName() = "PyOperatorInspection"
}
