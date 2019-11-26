package org.jetbrains.research.deepbugs.common.ide.fus.service.log

import com.intellij.internal.statistic.beans.MetricEvent
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.internal.statistic.utils.StatisticsUploadAssistant.LOCK
import com.intellij.openapi.extensions.*
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.common.ide.fus.DeepBugsEventLogger
import org.jetbrains.research.deepbugs.common.ide.fus.collectors.state.ProjectStateCollector

class DeepBugsStateLogger {
    fun logProjectStates(project: Project) {
        synchronized(LOCK) {
            val extensions = Extensions.getRootArea().extensionPoints.filter { it.name.startsWith("dbp.statistics") }
            extensions.map { it.extension }.forEach { collector ->
                collector as ProjectStateCollector
                val group = EventLogGroup(collector.groupId, collector.version)
                logStateEvents(project, group, collector.getStates(project))
            }
        }
    }

    companion object {
        private const val INVOKED = "invoked"

        fun getInstance(): DeepBugsStateLogger {
            return DeepBugsStateLogger()
        }

        private fun logStateEvents(
            project: Project?,
            group: EventLogGroup,
            states: Set<MetricEvent>
        ) {
            val logger = DeepBugsEventLogger
            if (states.isNotEmpty()) {
                val groupData = FeatureUsageData().addProject(project)
                states.forEach { state ->
                    val data = mergeWithEventData(groupData, state.data)
                    val eventData = data?.build() ?: emptyMap()
                    logger.logState(group, state.eventId, eventData)
                }
            }
            logger.logState(group, INVOKED, FeatureUsageData().addProject(project).build())
        }

        private fun mergeWithEventData(groupData: FeatureUsageData?, data: FeatureUsageData?): FeatureUsageData? {
            data ?: return groupData

            val newData = groupData?.copy() ?: FeatureUsageData()
            newData.merge(data, "event_")
            return newData
        }
    }
}
