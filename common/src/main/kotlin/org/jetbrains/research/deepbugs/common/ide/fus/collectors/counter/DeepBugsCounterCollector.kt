package org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter

import com.intellij.concurrency.JobScheduler
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.ide.fus.DeepBugsEventLogger
import java.util.*
import java.util.concurrent.TimeUnit

object DeepBugsCounterCollector {
    private const val LOG_DELAY_MIN = 24 * 60
    private const val LOG_INITIAL_DELAY_MIN = 10

    private val eventGroups = HashMap<String, EventLogGroup>()

    init {
        for (group in DeepBugsEventLogger.counterGroups) register(group)

        JobScheduler.getScheduler().scheduleWithFixedDelay(
            { trackRegisteredGroups() },
            LOG_INITIAL_DELAY_MIN.toLong(),
            LOG_DELAY_MIN.toLong(),
            TimeUnit.MINUTES
        )
    }

    fun problemFound(project: Project, inspection: String, result: Float) = log("dbp.inspections", "report") {
        addProject(project)
        addData("inspection", inspection)
        addData("result", result)
    }

    fun embeddingMatched(project: Project, inspection: String, matched: Boolean) = log("dbp.inspections", "embedding.matched") {
        addProject(project)
        addData("inspection", inspection)
        addData("matched", matched)
    }

    fun quickFixApplied(project: Project, quickFixId: String, cancelled: Boolean) = log("dbp.inspections", "quickfix.applied") {
        addProject(project)
        addData("plugin", DeepBugsPlugin.pluginId)
        addData("quickfix", quickFixId)
        addData("cancelled", cancelled)
    }

    fun checkDisabled(project: Project, total: Int) = log("dbp.settings", "check.disabled") {
        addProject(project)
        addData("plugin", DeepBugsPlugin.pluginId)
        addData("total", total)
    }

    fun modelReset(total: Int) = log("dbp.settings", "model.reset") {
        addData("plugin", DeepBugsPlugin.pluginId)
        addData("total", total)
    }

    private fun log(groupId: String, eventId: String, body: FeatureUsageData.() -> Unit) {
        return DeepBugsEventLogger.log(eventGroups[groupId] ?: return, eventId, FeatureUsageData().apply(body))
    }

    private fun register(groupId: String) {
        eventGroups[groupId] = EventLogGroup(groupId, DeepBugsEventLogger.version)
    }

    private fun trackRegisteredGroups() = eventGroups.values.forEach { group -> DeepBugsEventLogger.log(group, "registered") }
}
