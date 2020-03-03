package org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter

import com.intellij.concurrency.JobScheduler
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.ide.fus.DeepBugsEventLogger
import java.util.concurrent.TimeUnit

object DeepBugsCounterCollector {
    private const val LOG_DELAY_MIN = 24 * 60
    private const val LOG_INITIAL_DELAY_MIN = 10

    private val eventGroup = EventLogGroup("dbp.count", DeepBugsEventLogger.version)

    init {
        JobScheduler.getScheduler().scheduleWithFixedDelay(
            { trackRegistered() },
            LOG_INITIAL_DELAY_MIN.toLong(),
            LOG_DELAY_MIN.toLong(),
            TimeUnit.MINUTES
        )
    }

    fun problemFound(project: Project, inspection: String, result: Float) = log("report") {
        addPluginInfo(DeepBugsPlugin.info)
        addProject(project)
        addData("inspection", inspection)
        addData("result", result)
    }

    fun tokensMatched(project: Project, inspection: String, matched: Boolean) = log("tokens.matched") {
        addPluginInfo(DeepBugsPlugin.info)
        addProject(project)
        addData("inspection", inspection)
        addData("matched", matched)
    }

    fun quickFixApplied(project: Project, quickFixId: String, cancelled: Boolean) = log("quickfix.applied") {
        addPluginInfo(DeepBugsPlugin.info)
        addProject(project)
        addData("quickfix", quickFixId)
        addData("cancelled", cancelled)
    }

    fun checkDisabled(project: Project, total: Int) = log("check.disabled") {
        addPluginInfo(DeepBugsPlugin.info)
        addProject(project)
        addData("total", total)
    }

    fun modelReset(total: Int) = log("model.reset") {
        addPluginInfo(DeepBugsPlugin.info)
        addData("total", total)
    }

    private fun log(eventId: String, body: FeatureUsageData.() -> Unit) {
        return DeepBugsEventLogger.log(eventGroup, eventId, FeatureUsageData().apply(body))
    }

    private fun trackRegistered() = DeepBugsEventLogger.log(eventGroup, "registered")
}
