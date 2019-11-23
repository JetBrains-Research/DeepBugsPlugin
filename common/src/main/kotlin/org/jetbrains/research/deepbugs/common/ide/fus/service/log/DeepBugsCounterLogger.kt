package org.jetbrains.research.deepbugs.common.ide.fus.service.log

import com.intellij.concurrency.JobScheduler
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

import org.jetbrains.research.deepbugs.common.ide.fus.DeepBugsEventLogger

import java.util.*
import java.util.concurrent.TimeUnit

object DeepBugsCounterLogger {
    private const val REGISTERED = "registered"

    private const val LOG_DELAY_MIN = 24 * 60
    private const val LOG_INITIAL_DELAY_MIN = 1

    private val eventGroups = HashMap<String, EventLogGroup>()
    private val LOG = Logger.getInstance("org.jetbrains.research.deepbugs.services.logger.service.log.DeepBugsCounterLogger")

    init {
        val version = DeepBugsEventLogger.getConfig().version
        GeneralCounterGroups.values().forEach { group ->
            register(EventLogGroup(group.groupId, version))
        }

        JobScheduler.getScheduler().scheduleWithFixedDelay(
            { trackRegisteredGroups() },
            LOG_INITIAL_DELAY_MIN.toLong(),
            LOG_DELAY_MIN.toLong(),
            TimeUnit.MINUTES
        )
    }

    fun logEvent(
        project: Project,
        group: GeneralCounterGroups,
        eventId: String,
        data: FeatureUsageData
    ) {
        val eventGroup = findRegisteredGroupById(group.groupId)
            ?: return
        DeepBugsEventLogger.log(eventGroup, eventId, data.addProject(project).build())
    }

    fun logEvent(
        group: GeneralCounterGroups,
        eventId: String,
        data: FeatureUsageData
    ) {
        val eventGroup = findRegisteredGroupById(group.groupId) ?: return
        DeepBugsEventLogger.log(eventGroup, eventId, data.build())
    }

    private fun register(group: EventLogGroup) {
        eventGroups[group.id] = group
    }

    private fun trackRegisteredGroups() {
        eventGroups.values.forEach { group ->
            DeepBugsEventLogger.log(group, REGISTERED)
        }
    }

    private fun findRegisteredGroupById(groupId: String): EventLogGroup? {
        if (!eventGroups.containsKey(groupId)) {
            LOG.warn("Group '$groupId' is not registered.")
            return null
        }
        return eventGroups[groupId]
    }
}
