package org.jetbrains.research.groups.ml_methods.deepbugs.python.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

class PyDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener {
            binOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
        }
        defaultBinOperandThreshold.addActionListener {
            binOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
        }
        defaultSwappedArgsThreshold.addActionListener {
            swappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
        }
        defaultAll.addActionListener {
            binOperatorThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERATOR_CONFIG
            binOperandThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_BIN_OPERAND_CONFIG
            swappedArgsThreshold = PyDeepBugsInspectionConfigurable.PY_DEFAULT_SWAPPED_ARGS_CONFIG
        }
    }
}