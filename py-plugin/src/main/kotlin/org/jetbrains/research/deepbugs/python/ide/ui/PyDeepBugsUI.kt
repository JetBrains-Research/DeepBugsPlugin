package org.jetbrains.research.deepbugs.python.ide.ui

import org.jetbrains.research.deepbugs.common.ide.fus.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.deepbugs.common.ide.ui.DeepBugsUI
import org.jetbrains.research.deepbugs.python.PyDeepBugsConfig

class PyDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener {
            binOperatorThreshold = PyDeepBugsConfig.default.binOperatorThreshold
            SettingsStatsCollector.logDefaultOperator(COMPONENT_NAME)
        }
        defaultBinOperandThreshold.addActionListener {
            binOperandThreshold = PyDeepBugsConfig.default.binOperandThreshold
            SettingsStatsCollector.logDefaultOperand(COMPONENT_NAME)
        }
        defaultSwappedArgsThreshold.addActionListener {
            swappedArgsThreshold = PyDeepBugsConfig.default.swappedArgsThreshold
            SettingsStatsCollector.logDefaultCall(COMPONENT_NAME)
        }
        defaultAll.addActionListener {
            binOperatorThreshold = PyDeepBugsConfig.default.binOperatorThreshold
            binOperandThreshold = PyDeepBugsConfig.default.binOperandThreshold
            swappedArgsThreshold = PyDeepBugsConfig.default.swappedArgsThreshold
            SettingsStatsCollector.logDefaultAll(COMPONENT_NAME)
        }
    }

    companion object {
        const val COMPONENT_NAME = "PyInspectionConfig"
    }
}
