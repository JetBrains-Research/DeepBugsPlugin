package org.jetbrains.research.deepbugs.common.settings

data class DeepBugsState(
    var curBinOperatorThreshold: Float = 0.0f,
    var curBinOperandThreshold: Float = 0.0f,
    var curSwappedArgsThreshold: Float = 0.0f
)
