package org.jetbrains.research.deepbugs.python.ide.inspections

import org.jetbrains.research.deepbugs.python.*
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsCallExprInspection

class PyDeepBugsSwappedArgsInspection : PyDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.python.swapped.args.inspection.warning"

    override fun getModel() = PyModelManager.storage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsConfig.get().swappedArgsThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.swapped.args.inspection.display")
    override fun getShortName() = "PyCallInspection"
}
