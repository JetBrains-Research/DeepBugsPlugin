package org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData

import org.jetbrains.research.deepbugs.common.ide.fus.service.log.DeepBugsCounterLogger
import org.jetbrains.research.deepbugs.common.ide.fus.service.log.GeneralCounterGroups.ERRORS

object ErrorInfoCollector {
    private const val INIT_ERROR = "init.error"

    fun logInitErrorReported() {
        logErrorWithStatus("reported")
    }

    @Suppress("unused")
    fun logInitErrorSubmitted() {
        logErrorWithStatus("submitted")
    }

    private fun logErrorWithStatus(status: String) {
        val data = FeatureUsageData().addData("status", status)
        DeepBugsCounterLogger.logEvent(ERRORS, INIT_ERROR, data)
    }
}
