package org.jetbrains.research.groups.ml_methods.deepbugs.services.logging

data class BugFeatures(
        val exprType: String,
        val bugType: String,
        val probability: Float,
        val curThreshold: Float
): LogData