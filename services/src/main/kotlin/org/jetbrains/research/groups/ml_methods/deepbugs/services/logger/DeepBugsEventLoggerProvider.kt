package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger

import com.intellij.internal.statistic.eventLog.StatisticsEventLoggerProvider
import com.intellij.internal.statistic.utils.StatisticsUploadAssistant
import com.intellij.openapi.application.ApplicationManager

class DeepBugsEventLoggerProvider : StatisticsEventLoggerProvider("DBP", 1) {
    override fun isRecordEnabled(): Boolean =
            !ApplicationManager.getApplication().isUnitTestMode &&
                StatisticsUploadAssistant.isCollectAllowed()

    override fun isSendEnabled(): Boolean =
            isRecordEnabled() && StatisticsUploadAssistant.isSendAllowed()
}