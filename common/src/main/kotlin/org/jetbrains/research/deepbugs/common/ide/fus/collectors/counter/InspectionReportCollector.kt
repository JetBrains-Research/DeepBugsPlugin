package org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.project.Project

import org.jetbrains.research.deepbugs.common.ide.fus.service.log.DeepBugsCounterLogger
import org.jetbrains.research.deepbugs.common.ide.fus.service.log.GeneralCounterGroups.INSPECTION_REPORT

object InspectionReportCollector {
    private const val REPORT_EVENT = "report"

    fun logReport(project: Project, inspection: String, result: Float) {
        val data = FeatureUsageData()
            .addData("inspection", inspection)
            .addData("result", result)
        DeepBugsCounterLogger.logEvent(project, INSPECTION_REPORT, REPORT_EVENT, data)
    }
}
