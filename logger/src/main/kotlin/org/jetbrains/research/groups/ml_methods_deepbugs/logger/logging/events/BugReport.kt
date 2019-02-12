package org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events

data class BugReport(
        val exprType: String,
        val inspection: String,
        val probability: Float
): LogData