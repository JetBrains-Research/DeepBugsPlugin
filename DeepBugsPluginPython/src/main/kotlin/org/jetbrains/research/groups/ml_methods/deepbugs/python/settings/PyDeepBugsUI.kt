package org.jetbrains.research.groups.ml_methods.deepbugs.python.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

class PyDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener(
                { binOperatorThreshold = PyDeepBugsInspectionConfigurable.defaultBinOperatorConfig })
        defaultBinOperandThreshold.addActionListener(
                { binOperandThreshold = PyDeepBugsInspectionConfigurable.defaultBinOperandConfig })
        defaultSwappedArgsThreshold.addActionListener(
                { swappedArgsThreshold = PyDeepBugsInspectionConfigurable.defaultSwappedArgsConfig })
        defaultAll.addActionListener(
                {
                    binOperatorThreshold = PyDeepBugsInspectionConfigurable.defaultBinOperatorConfig
                    binOperandThreshold = PyDeepBugsInspectionConfigurable.defaultBinOperandConfig
                    swappedArgsThreshold = PyDeepBugsInspectionConfigurable.defaultSwappedArgsConfig
                })
    }
}