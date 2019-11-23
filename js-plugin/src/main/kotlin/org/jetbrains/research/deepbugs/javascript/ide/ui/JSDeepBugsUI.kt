package org.jetbrains.research.deepbugs.javascript.ide.ui

import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsUI
import org.jetbrains.research.deepbugs.javascript.JSDeepBugsConfig

class JSDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener {
            binOperatorThreshold = JSDeepBugsConfig.default.binOperatorThreshold
            SettingsStatsCollector.logDefaultOperator(COMPONENT_NAME)
        }
        defaultBinOperandThreshold.addActionListener {
            binOperandThreshold = JSDeepBugsConfig.default.binOperandThreshold
            SettingsStatsCollector.logDefaultOperand(COMPONENT_NAME)
        }
        defaultSwappedArgsThreshold.addActionListener {
            swappedArgsThreshold = JSDeepBugsConfig.default.swappedArgsThreshold
            SettingsStatsCollector.logDefaultCall(COMPONENT_NAME)
        }
        defaultAll.addActionListener {
            binOperatorThreshold = JSDeepBugsConfig.default.binOperatorThreshold
            binOperandThreshold = JSDeepBugsConfig.default.binOperandThreshold
            swappedArgsThreshold = JSDeepBugsConfig.default.swappedArgsThreshold
            SettingsStatsCollector.logDefaultAll(COMPONENT_NAME)
        }
    }

    companion object {
        const val COMPONENT_NAME = "JSInspectionConfig"
    }
}
