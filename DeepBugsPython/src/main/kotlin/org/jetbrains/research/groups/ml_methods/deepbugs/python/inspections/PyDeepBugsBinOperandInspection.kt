package org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections

import com.intellij.openapi.project.Project
import org.jetbrains.research.groups.ml_methods.deepbugs.python.inspections.base.PyDeepBugsBinExprInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.InspectionReportCollector
import org.jetbrains.research.groups.ml_methods.deepbugs.python.settings.PyDeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService.models
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService.EVENT_LOG_PREFIX

class PyDeepBugsBinOperandInspection : PyDeepBugsBinExprInspection() {
    override val keyMessage: String = "deepbugs.python.bin.operand.inspection.warning"

    override fun getModel() = models.binOperandModel
    override fun getThreshold(): Float = PyDeepBugsInspectionConfig.getInstance().curBinOperandThreshold

    override fun getDisplayName() = DeepBugsPythonBundle.message("deepbugs.python.bin.operand.inspection.display")
    override fun getShortName(): String = "PyDeepBugsBinOperand"

    override fun logReport(project: Project, result: Float) {
        InspectionReportCollector.logOperandInspectionReport(project, EVENT_LOG_PREFIX, result)
    }
}