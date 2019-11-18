package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.counter.SettingsStatsCollector
import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

class JSDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener {
            binOperatorThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERATOR_CONFIG
            SettingsStatsCollector.logDefaultOperator(COMPONENT_NAME)
        }
        defaultBinOperandThreshold.addActionListener {
            binOperandThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERAND_CONFIG
            SettingsStatsCollector.logDefaultOperand(COMPONENT_NAME)
        }
        defaultSwappedArgsThreshold.addActionListener {
            swappedArgsThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_SWAPPED_ARGS_CONFIG
            SettingsStatsCollector.logDefaultCall(COMPONENT_NAME)
        }
        defaultAll.addActionListener {
            binOperatorThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERATOR_CONFIG
            binOperandThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_BIN_OPERAND_CONFIG
            swappedArgsThreshold = JSDeepBugsInspectionConfigurable.JS_DEFAULT_SWAPPED_ARGS_CONFIG
            SettingsStatsCollector.logDefaultAll(COMPONENT_NAME)
        }
    }

    companion object {
        const val COMPONENT_NAME = "JSInspectionConfig"
    }
}