package org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format

import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.EventId
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.events.EventData

data class CounterLogEventInfo(
        val id: String = EventId.BUG_REPORT.id,
        val data: EventData,
        var count: Int = 1
) : LogEventInfo

data class StateLogEventInfo(
        val id: String = EventId.THRESHOLD_CONFIGURED.id,
        val data: EventData,
        var state: Boolean = true
) : LogEventInfo