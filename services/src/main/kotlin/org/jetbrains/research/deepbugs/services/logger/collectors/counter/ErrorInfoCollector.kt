package org.jetbrains.research.deepbugs.services.logger.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData

import org.jetbrains.research.deepbugs.services.logger.service.log.DeepBugsCounterLogger
import org.jetbrains.research.deepbugs.services.logger.service.log.GeneralCounterGroups.ERRORS

object ErrorInfoCollector {
    private const val INIT_ERROR = "init.error"

    fun logInitErrorReported() {
        logErrorWithStatus("reported")
    }

    fun logInitErrorSubmitted() {
        logErrorWithStatus("submitted")
    }

    private fun logErrorWithStatus(status: String) {
        val data = FeatureUsageData().addData("status", status)
        DeepBugsCounterLogger.logEvent(ERRORS, INIT_ERROR, data)
    }
}
