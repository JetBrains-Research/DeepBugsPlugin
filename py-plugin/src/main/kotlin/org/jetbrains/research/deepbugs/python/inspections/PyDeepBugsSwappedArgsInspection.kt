package org.jetbrains.research.deepbugs.python.inspections

import org.jetbrains.research.deepbugs.python.PyModelManager
import org.jetbrains.research.deepbugs.python.inspections.base.PyDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.python.PyResourceBundle

class PyDeepBugsSwappedArgsInspection : PyDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.python.swapped.args.inspection.warning"

    override fun getModel() = PyModelManager.storage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold

    override fun getDisplayName() = PyResourceBundle.message("deepbugs.python.swapped.args.inspection.display")
    override fun getShortName() = "PyCallInspection"
}
