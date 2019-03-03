package org.jetbrains.research.groups.ml_methods.deepbugs.logger.log_reporter

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.PermanentInstallationID
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.*
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.events.ThresholdConfigured
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format.*
import java.util.*

class DeepBugsLogService {
    private val recorder = "DeepBugs"
    private val recorderVersion = "1"
    private val session = UUID.randomUUID().toString()
    private val userId = PermanentInstallationID.get()
    private val build = ApplicationInfo.getInstance().build

    private val logStorage = LogRecordCollector(recorder, build.productCode, userId)

    fun sendInspectionLog(eventGroup: EventGroup, data: BugReport) {
        val eventData = CounterLogEventInfo(data = data, count = 1)
        val toReport = createLogRecord(eventGroup, eventData)
        logStorage.appendReport(toReport)
    }

    fun sendConfigLog(eventGroup: EventGroup, data: ThresholdConfigured) {
        val eventData = StateLogEventInfo(data = data)
        val toReport = createLogRecord(eventGroup, eventData)
        logStorage.appendReport(toReport)
    }

    private fun createLogRecord(eventType: EventGroup, eventInfo: LogEventInfo): LogEvent {
        val timestamp = System.currentTimeMillis()
        return LogEvent(
                recorder_version = recorderVersion,
                build = build.asStringWithoutProductCodeAndSnapshot(),
                time = timestamp,
                session = session,
                group = EventLogGroup(eventType.id, eventType.version),
                event = eventInfo
        )
    }
}