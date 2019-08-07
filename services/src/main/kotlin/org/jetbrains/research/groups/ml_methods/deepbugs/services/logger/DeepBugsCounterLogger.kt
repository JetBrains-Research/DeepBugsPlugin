package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger

import com.intellij.concurrency.JobScheduler
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.util.*
import java.util.concurrent.TimeUnit

enum class GeneralCounterGroups(val groupId: String) {
    INSPECTION_REPORT("inspection.report"),
    UI_INVOKED("settings.ui.invoked"),
    TO_DEFAULT("settings.default"),
    CONFIGURED("settings.configured"),
}

object DeepBugsCounterLogger {
    private const val REGISTERED = "registered"

    private const val LOG_DELAY_MIN = 24 * 60
    private const val LOG_INITIAL_DELAY_MIN = 1

    private val eventGroups = HashMap<String, EventLogGroup>()
    private val LOG = Logger.getInstance("org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.DeepBugsCounterLogger")

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
            groupId: String,
            eventId: String
    ) {
        val group = findRegisteredGroupById(groupId) ?: return
        val data = FeatureUsageData().addProject(project).build()
        DeepBugsEventLogger.log(group, eventId, data)
    }

    fun logEvent(
            project: Project,
            groupId: String,
            eventId: String,
            data: FeatureUsageData
    ) {
        val group = findRegisteredGroupById(groupId) ?: return
        DeepBugsEventLogger.log(group, eventId, data.addProject(project).build())
    }

    fun logEvent(
            groupId: String,
            eventId: String
    ) {
        val group = findRegisteredGroupById(groupId) ?: return
        DeepBugsEventLogger.log(group, eventId)
    }

    fun logEvent(
            groupId: String,
            eventId: String,
            data: FeatureUsageData
    ) {
        val group = findRegisteredGroupById(groupId) ?: return
        DeepBugsEventLogger.log(group, eventId, data.build())
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