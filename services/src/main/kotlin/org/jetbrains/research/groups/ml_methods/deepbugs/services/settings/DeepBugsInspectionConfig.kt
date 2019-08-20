package org.jetbrains.research.groups.ml_methods.deepbugs.services.settings

interface DeepBugsInspectionConfig {
    val configId: String

    var curBinOperatorThreshold: Float
    var curBinOperandThreshold: Float
    var curSwappedArgsThreshold: Float
}