package org.jetbrains.research.groups.ml_methods.deepbugs.services.settings

interface DeepBugsInspectionConfig {
    var curBinOperatorThreshold: Float
    var curBinOperandThreshold: Float
    var curSwappedArgsThreshold: Float
}