package org.jetbrains.research.deepbugs.common.logger.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import org.jetbrains.research.deepbugs.common.logger.service.log.DeepBugsCounterLogger
import org.jetbrains.research.deepbugs.common.logger.service.log.GeneralCounterGroups.SETTINGS
import org.jetbrains.research.deepbugs.common.logger.service.log.GeneralCounterGroups.SETTINGS_UI
import org.jetbrains.research.deepbugs.common.settings.DeepBugsInspectionConfig
import org.jetbrains.research.deepbugs.common.ui.DeepBugsUI

object SettingsStatsCollector {
    private const val SETTINGS_DEFAULT = "set.to.default"
    private const val CONFIG_THRESHOLD = "config.metrics.threshold"

    private val SUPPORTED_INSPECTION_CLASSES = arrayOf("operator", "operand", "call")

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
        DeepBugsCounterLogger.logEvent(SETTINGS, SETTINGS_DEFAULT, data)
    }

    fun logNewSettings(configurable: DeepBugsInspectionConfig, ui: DeepBugsUI) {
        generateConfigEventData(configurable, ui).forEach { data ->
            DeepBugsCounterLogger.logEvent(SETTINGS, CONFIG_THRESHOLD, data)
        }
    }

    private fun generateConfigEventData(config: DeepBugsInspectionConfig, ui: DeepBugsUI): List<FeatureUsageData> {
        return SUPPORTED_INSPECTION_CLASSES.map { generateConfigData(it, config, ui) }
    }

    private fun generateConfigData(
        inspectionClass: String,
        config: DeepBugsInspectionConfig,
        ui: DeepBugsUI
    ): FeatureUsageData {
        val (prevConfig: Float, newConfig: Float) = when (inspectionClass) {
            "operator" -> Pair(config.curState.curBinOperatorThreshold, ui.binOperatorThreshold)
            "operand" -> Pair(config.curState.curBinOperandThreshold, ui.binOperandThreshold)
            "call" -> Pair(config.curState.curSwappedArgsThreshold, ui.swappedArgsThreshold)
            else -> throw IllegalArgumentException("Inspection call $inspectionClass is not supported")
        }
        return FeatureUsageData()
            .addData("component", config.configId)
            .addData("related_inspection", inspectionClass)
            .addData("prev_config", prevConfig)
            .addData("new_config", newConfig)
    }
}
