package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.counter

import com.intellij.internal.statistic.eventLog.FeatureUsageData

import org.jetbrains.research.groups.ml_methods.deepbugs.services.settings.DeepBugsInspectionConfig
import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.service.log.DeepBugsCounterLogger
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.service.log.GeneralCounterGroups.*

object SettingsStatsCollector {
    fun logSettingsInvoked(prefix: String) {
        DeepBugsCounterLogger.logEvent(UI_INVOKED.groupId, "$prefix-settings-ui-invoked")
    }

    fun logDefaultOperator(prefix: String) {
        DeepBugsCounterLogger.logEvent(TO_DEFAULT.groupId, "$prefix-operator-to-default")
    }

    fun logDefaultOperand(prefix: String) {
        DeepBugsCounterLogger.logEvent(TO_DEFAULT.groupId, "$prefix-operand-to-default")
    }

    fun logDefaultCall(prefix: String) {
        DeepBugsCounterLogger.logEvent(TO_DEFAULT.groupId, "$prefix-call-to-default")
    }

    fun logDefaultAll(prefix: String) {
        DeepBugsCounterLogger.logEvent(TO_DEFAULT.groupId, "$prefix-all-to-default")
    }

    fun logNewSettings(prefix: String, configurable: DeepBugsInspectionConfig, ui: DeepBugsUI) {
        val data = FeatureUsageData()
                .addData("old-operator-config", configurable.curBinOperatorThreshold)
                .addData("old-operand-config", configurable.curBinOperandThreshold)
                .addData("old-call-config", configurable.curSwappedArgsThreshold)
                .addData("new-operator-config", ui.binOperatorThreshold)
                .addData("new-operand-config", ui.binOperandThreshold)
                .addData("new-call-config", ui.swappedArgsThreshold)
        DeepBugsCounterLogger.logEvent(CONFIGURED.groupId, "$prefix-settings-configured", data)
    }
}