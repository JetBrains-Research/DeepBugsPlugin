package org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format

import com.google.gson.GsonBuilder

val gson by lazy { GsonBuilder().setPrettyPrinting().create() }

data class LogReport(
        var recorder: String,
        var product: String,
        var device: String,
        var records: MutableList<LogRecord> = mutableListOf()
)

data class LogRecord(var events: MutableList<LogEvent> = mutableListOf())

data class LogEvent(
        var recorder_version: String,
        var build: String,
        var time: Long,
        var session: String,
        var group: EventLogGroup,
        var bucket: String = "-1",
        var event: LogEventInfo
)

data class EventLogGroup(var id: String, var version: String)



