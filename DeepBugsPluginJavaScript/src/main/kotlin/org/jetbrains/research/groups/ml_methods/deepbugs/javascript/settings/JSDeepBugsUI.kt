package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.settings

import org.jetbrains.research.groups.ml_methods.deepbugs.services.ui.DeepBugsUI

class JSDeepBugsUI : DeepBugsUI() {
    init {
        defaultBinOperatorThreshold.addActionListener(
                { binOperatorThreshold = JSDeepBugsInspectionConfigurable.defaultBinOperatorConfig })
        defaultBinOperandThreshold.addActionListener(
                { binOperandThreshold = JSDeepBugsInspectionConfigurable.defaultBinOperandConfig })
        defaultSwappedArgsThreshold.addActionListener(
                { swappedArgsThreshold = JSDeepBugsInspectionConfigurable.defaultSwappedArgsConfig })
        defaultAll.addActionListener(
                {
                    binOperatorThreshold = JSDeepBugsInspectionConfigurable.defaultBinOperatorConfig
                    binOperandThreshold = JSDeepBugsInspectionConfigurable.defaultBinOperandConfig
                    swappedArgsThreshold = JSDeepBugsInspectionConfigurable.defaultSwappedArgsConfig
                })
    }
}