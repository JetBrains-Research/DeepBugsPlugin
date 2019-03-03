package org.jetbrains.research.groups.ml_methods.deepbugs.logger.log_reporter

import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format.LogEvent
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format.LogRecord
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format.LogReport
import org.jetbrains.research.groups.ml_methods.deepbugs.logger.logging.report_format.gson
import kotlin.concurrent.thread

internal class LogRecordCollector(private val recorder: String, private val product: String, private val device: String) {
    private var collectedReports = LogReport(recorder, product, device)
    private var currentRecord = LogRecord()

    private var lastSendingThread: Thread? = null

    fun appendReport(report: LogEvent) {
        if (currentRecord.events.size == MAX_RECORD_SIZE)
            dumpCurrentRecord()
        currentRecord.events.add(report)
    }

    private fun dumpCurrentRecord() {
        if (collectedReports.records.size == MAX_REPORT_SIZE)
            dump()
        collectedReports.records.add(currentRecord)
        currentRecord = LogRecord()
    }

    fun dump() {
        val reportsToSend = gson.toJson(collectedReports)
        collectedReports = LogReport(recorder, product, device)
        lastSendingThread = thread {
            //TODO: implement log reporter
            TestDeepBugsLogReporter.send(reportsToSend)
        }
    }

    fun completeReporting() {
        lastSendingThread?.join()
    }

    companion object {
        //test size
        const val MAX_RECORD_SIZE = 5
        const val MAX_REPORT_SIZE = 5
    }
}