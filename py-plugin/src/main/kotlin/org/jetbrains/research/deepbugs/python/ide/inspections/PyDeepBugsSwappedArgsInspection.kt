package org.jetbrains.research.deepbugs.python.ide.inspections

import org.jetbrains.research.deepbugs.common.model.ModelManager
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle
import org.jetbrains.research.deepbugs.python.ide.inspections.base.PyDeepBugsCallExprInspection

class PyDeepBugsSwappedArgsInspection : PyDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.python.swapped.args.inspection.warning"

    override fun getModel() = ModelManager.storage.swappedArgsModel
    override fun getThreshold() = PyDeepBugsConfig.get().swappedArgsThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.swapped.args.inspection.display")
    override fun getShortName() = "PyCallInspection"
}
