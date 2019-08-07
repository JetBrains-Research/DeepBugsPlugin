package org.jetbrains.research.groups.ml_methods.deepbugs.python.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.SettingsInfoCollector
import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonService.EVENT_LOG_PREFIX

class PyDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener {
            binOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
            SettingsInfoCollector.logDefaultOperator(EVENT_LOG_PREFIX)
        }
        defaultBinOperandThreshold.addActionListener {
            binOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
            SettingsInfoCollector.logDefaultOperand(EVENT_LOG_PREFIX)
        }
        defaultSwappedArgsThreshold.addActionListener {
            swappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
            SettingsInfoCollector.logDefaultCall(EVENT_LOG_PREFIX)
        }
        defaultAll.addActionListener {
            binOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
            binOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
            swappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
            SettingsInfoCollector.logDefaultAll(EVENT_LOG_PREFIX)
        }
    }
}