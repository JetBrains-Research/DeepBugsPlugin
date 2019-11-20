package org.jetbrains.research.deepbugs.common.settings

interface DeepBugsInspectionConfig {
    val configId: String

    var curBinOperatorThreshold: Float
    var curBinOperandThreshold: Float
    var curSwappedArgsThreshold: Float
}
