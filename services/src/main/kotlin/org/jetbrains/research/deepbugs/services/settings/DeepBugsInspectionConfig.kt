package org.jetbrains.research.deepbugs.services.settings

interface DeepBugsInspectionConfig {
    val configId: String

    var curBinOperatorThreshold: Float
    var curBinOperandThreshold: Float
    var curSwappedArgsThreshold: Float
}
