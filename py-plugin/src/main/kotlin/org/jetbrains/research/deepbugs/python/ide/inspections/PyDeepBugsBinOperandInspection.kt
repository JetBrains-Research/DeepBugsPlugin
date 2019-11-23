package org.jetbrains.research.deepbugs.python.ide.inspections

import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage = "deepbugs.python.bin.operand.inspection.warning"

    override fun getModel() = ModelManager.storage?.binOperandModel
    override fun getThreshold() = PyDeepBugsConfig.get().binOperandThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.bin.operand.inspection.display")
    override fun getShortName() = "PyOperandInspection"
}
