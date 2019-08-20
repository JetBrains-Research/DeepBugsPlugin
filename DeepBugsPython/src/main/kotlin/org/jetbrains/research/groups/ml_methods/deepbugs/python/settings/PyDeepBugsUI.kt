package org.jetbrains.research.groups.ml_methods.deepbugs.python.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

class PyDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener {
            binOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
            SettingsStatsCollector.logDefaultOperator(COMPONENT_NAME)
        }
        defaultBinOperandThreshold.addActionListener {
            binOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
            SettingsStatsCollector.logDefaultOperand(COMPONENT_NAME)
        }
        defaultSwappedArgsThreshold.addActionListener {
            swappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
            SettingsStatsCollector.logDefaultCall(COMPONENT_NAME)
        }
        defaultAll.addActionListener {
            binOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
            binOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
            swappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
            SettingsStatsCollector.logDefaultAll(COMPONENT_NAME)
        }
    }

    companion object {
        const val COMPONENT_NAME = "PyInspectionConfig"
    }
}