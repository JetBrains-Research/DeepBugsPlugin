package org.jetbrains.research.deepbugs.common.ide.fus

import com.intellij.internal.statistic.eventLog.StatisticsEventLoggerProvider
import com.intellij.internal.statistic.utils.StatisticsUploadAssistant
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.registry.Registry

class DeepBugsEventLoggerProvider : StatisticsEventLoggerProvider("DBP", 1) {
    override fun isRecordEnabled(): Boolean = false
        //!ApplicationManager.getApplication().isUnitTestMode &&
        //    Registry.`is`("feature.usage.event.log.collect.and.upload") &&
        //    StatisticsUploadAssistant.isCollectAllowed()

    override fun isSendEnabled(): Boolean = false
        //isRecordEnabled() && StatisticsUploadAssistant.isSendAllowed()
}
