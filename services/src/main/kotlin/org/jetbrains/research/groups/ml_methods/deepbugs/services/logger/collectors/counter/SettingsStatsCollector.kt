package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.service.log.DeepBugsCounterLogger
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.service.log.GeneralCounterGroups.SETTINGS
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.service.log.GeneralCounterGroups.SETTINGS_UI
import org.jetbrains.research.groups.ml_methods.deepbugs.services.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

object SettingsStatsCollector {
    fun logSettingsInvoked(componentId: String) {
        val data = FeatureUsageData().addData("component", componentId)
        DeepBugsCounterLogger.logEvent(SETTINGS_UI, "settings.ui.invoked", data)
    }

    fun logDefaultOperator(configId: String) {
        logDefault(configId, "operator")
    }

    fun logDefaultOperand(configId: String) {
        logDefault(configId, "operand")
    }

    fun logDefaultCall(configId: String) {
        logDefault(configId, "call")
    }

    fun logDefaultAll(configId: String) {
        logDefault(configId, "all")
    }

    private fun logDefault(componentId: String, relatedInspection: String) {
        val data = FeatureUsageData()
                .addData("component", componentId)
                .addData("related_inspection", relatedInspection)
        DeepBugsCounterLogger.logEvent(SETTINGS, "set.to.default", data)
    }

    fun logNewSettings(configurable: DeepBugsInspectionConfig, ui: DeepBugsUI) {
        generateEventListData(configurable, ui).forEach { data ->
            DeepBugsCounterLogger.logEvent(SETTINGS, "config.metrics.threshold", data)
        }
    }

    private fun generateEventListData(config: DeepBugsInspectionConfig, ui: DeepBugsUI): List<FeatureUsageData> {
        return listOf(
            FeatureUsageData()
                    .addData("component", config.configId)
                    .addData("related.inspection", "operator")
                    .addData("prev_config", config.curBinOperatorThreshold)
                    .addData("new_config", ui.binOperatorThreshold),
            FeatureUsageData()
                    .addData("component", config.configId)
                    .addData("related.inspection", "operand")
                    .addData("prev_config", config.curBinOperandThreshold)
                    .addData("new_config", ui.binOperandThreshold),
            FeatureUsageData()
                    .addData("component", config.configId)
                    .addData("related.inspection", "call")
                    .addData("prev_config", config.curSwappedArgsThreshold)
                    .addData("new_config", ui.swappedArgsThreshold)
        )
    }
}