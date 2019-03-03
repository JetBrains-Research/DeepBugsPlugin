package org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.events

data class ThresholdFeatures(
        val oldValue: Float,
        val newValue: Float
)

data class ThresholdConfigured(
        val binOperatorThreshold: ThresholdFeatures,
        val binOperandThreshold: ThresholdFeatures,
        val swappedArgsThreshold: ThresholdFeatures
): EventData