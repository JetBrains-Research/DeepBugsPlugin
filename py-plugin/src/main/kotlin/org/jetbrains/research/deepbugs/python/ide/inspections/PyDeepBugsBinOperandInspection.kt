package org.jetbrains.research.deepbugs.python.ide.inspections

import org.jetbrains.research.deepbugs.python.*
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operand.inspection.warning"

    override fun getModel() = PyModelManager.storage?.binOperandModel
    override fun getThreshold() = PyDeepBugsConfig.get().binOperandThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.bin.operand.inspection.display")
    override fun getShortName() = "PyOperandInspection"
}
