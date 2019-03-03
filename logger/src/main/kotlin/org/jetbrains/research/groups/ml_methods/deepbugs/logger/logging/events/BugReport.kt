package org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.events

data class BugReport(
        val inspection: String,
        val probability: Float
): EventData