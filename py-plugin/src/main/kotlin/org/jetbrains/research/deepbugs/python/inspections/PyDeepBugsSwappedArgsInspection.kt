package org.jetbrains.research.deepbugs.python.inspections

import org.jetbrains.research.deepbugs.python.inspections.base.PyDeepBugsCallExprInspection
import org.jetbrains.research.deepbugs.python.inspections.base.models
import org.jetbrains.research.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.python.utils.DeepBugsPythonBundle

class PyDeepBugsSwappedArgsInspection : PyDeepBugsCallExprInspection() {
    override val keyMessage = "deepbugs.python.swapped.args.inspection.warning"

    override fun getModel() = models.modelStorage?.binOperatorModel
    override fun getThreshold() = PyDeepBugsInspectionConfig.getInstance().curSwappedArgsThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("deepbugs.python.swapped.args.inspection.display")
    override fun getShortName() = "PyCallInspection"
}
