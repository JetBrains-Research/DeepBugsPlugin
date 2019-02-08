package org.jetbrains.research.groups.ml_methods.deepbugs.services.logging

data class ThresholdFeatures(
        val oldValue: Float,
        val newValue: Float
)

data class ThresholdConfigFeatures(
        val binOperatorThreshold: ThresholdFeatures,
        val binOperandThreshold: ThresholdFeatures,
        val swappedArgsThreshold: ThresholdFeatures
): LogData