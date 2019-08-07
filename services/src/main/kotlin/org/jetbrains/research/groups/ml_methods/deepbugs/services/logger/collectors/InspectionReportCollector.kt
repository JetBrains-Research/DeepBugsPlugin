package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.project.Project
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.DeepBugsCounterLogger
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.GeneralCounterGroups

object InspectionReportCollector {
    fun logSwappedArgsInspectionReport(project: Project, prefix: String, result: Float) {
        logReport(project, "$prefix-call-reported", result)
    }

    fun logOperatorInspectionReport(project: Project, prefix: String, result: Float) {
        logReport(project, "$prefix-operator-reported", result)
    }

    fun logOperandInspectionReport(project: Project, prefix: String, result: Float) {
        logReport(project, "$prefix-operand-reported", result)
    }

    private fun logReport(project: Project, inspection: String, result: Float) {
        val data = FeatureUsageData().addData("result", result)
        DeepBugsCounterLogger.logEvent(project, GeneralCounterGroups.INSPECTION_REPORT.groupId, inspection, data)
    }
}