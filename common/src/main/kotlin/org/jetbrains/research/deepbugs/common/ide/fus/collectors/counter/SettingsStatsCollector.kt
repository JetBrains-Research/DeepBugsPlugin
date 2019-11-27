package org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import org.jetbrains.research.deepbugs.common.ide.fus.service.log.DeepBugsCounterLogger
import org.jetbrains.research.deepbugs.common.ide.fus.service.log.GeneralCounterGroups.SETTINGS
import org.jetbrains.research.deepbugs.common.ide.fus.service.log.GeneralCounterGroups.SETTINGS_UI

object SettingsStatsCollector {
    private const val SETTINGS_DEFAULT = "set.to.default"

    fun logSettingsInvoked(componentId: String) {
        val data = FeatureUsageData().addData("component", componentId)
        DeepBugsCounterLogger.logEvent(SETTINGS_UI, "settings.ui.invoked", data)
    }

    fun logDefaultAll(configId: String) {
        logDefault(configId)
    }

    private fun logDefault(componentId: String) {
        val data = FeatureUsageData()
            .addData("component", componentId)
            .addData("related_inspection", "all")
        DeepBugsCounterLogger.logEvent(SETTINGS, SETTINGS_DEFAULT, data)
    }
}
