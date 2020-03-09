package org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter

import com.intellij.concurrency.JobScheduler
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import org.jetbrains.research.deepbugs.common.ide.fus.DeepBugsEventLogger
import java.util.concurrent.TimeUnit

object DeepBugsCounterCollector {
    private const val LOG_DELAY_MIN = 24 * 60
    private const val LOG_INITIAL_DELAY_MIN = 10
    private val LOG = Logger.getInstance("#com.intellij.internal.statistic.eventLog.StatisticsEventLogger")

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
        addProject(project)
        addData("inspection", inspection)
        addData("result", result)
    }

    fun tokensMatched(project: Project, inspection: String, matched: Boolean) = log("tokens.matched") {
        addProject(project)
        addData("inspection", inspection)
        addData("matched", matched)
    }

    fun quickFixApplied(project: Project, quickFixId: String, cancelled: Boolean) = log("quickfix.applied") {
        addProject(project)
        addData("quickfix", quickFixId)
        addData("cancelled", cancelled)
    }

    fun checkDisabled(project: Project, total: Int) = log("check.disabled") {
        addProject(project)
        addData("total", total)
    }

    fun modelReset(total: Int) = log("model.reset") {
        addData("total", total)
    }

    private fun log(eventId: String, body: FeatureUsageData.() -> Unit) {
        return try {
            val data = FeatureUsageData().addPluginInfo(DeepBugsPlugin.info).apply(body)
            DeepBugsEventLogger.log(eventGroup, eventId, data)
        } catch (ex: Exception) {
            LOG.warn(ex.message)
        }
    }

    private fun trackRegistered() = DeepBugsEventLogger.log(eventGroup, "registered")
}
