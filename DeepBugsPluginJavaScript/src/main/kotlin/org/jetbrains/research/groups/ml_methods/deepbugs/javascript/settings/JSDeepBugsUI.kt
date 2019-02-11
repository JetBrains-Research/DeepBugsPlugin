package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

class JSDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener { binOperatorThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERATOR_CONFIG }
        defaultBinOperandThreshold.addActionListener { binOperandThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERAND_CONFIG }
        defaultSwappedArgsThreshold.addActionListener { swappedArgsThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_SWAPPED_ARGS_CONFIG }
        defaultAll.addActionListener {
            binOperatorThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERATOR_CONFIG
            binOperandThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_BIN_OPERAND_CONFIG
            swappedArgsThreshold = JSDeepBugsInspectionConfigurable.DEFAULT_SWAPPED_ARGS_CONFIG
        }
    }
}