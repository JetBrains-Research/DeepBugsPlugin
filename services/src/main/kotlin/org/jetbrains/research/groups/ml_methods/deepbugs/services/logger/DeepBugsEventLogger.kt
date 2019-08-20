package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger

import com.intellij.internal.statistic.eventLog.EmptyStatisticsEventLogger
import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.eventLog.StatisticsEventLoggerProvider
import com.intellij.internal.statistic.eventLog.getEventLogProvider

object DeepBugsEventLogger {
    private val loggerProvider: StatisticsEventLoggerProvider = getEventLogProvider("DBP")

    fun log(group: EventLogGroup, action: String) {
        return loggerProvider.logger.log(group, action, false)
    }

    fun log(group: EventLogGroup, action: String, data: Map<String, Any>) {
        return loggerProvider.logger.log(group, action, data, false)
    }

    fun logState(group: EventLogGroup, action: String) {
        return loggerProvider.logger.log(group, action, true)
    }

    fun logState(group: EventLogGroup, action: String, data: Map<String, Any>) {
        return loggerProvider.logger.log(group, action, data, true)
    }

    fun getConfig(): StatisticsEventLoggerProvider {
        return loggerProvider
    }

    fun isEnabled(): Boolean {
        return loggerProvider.logger !is EmptyStatisticsEventLogger
    }
}