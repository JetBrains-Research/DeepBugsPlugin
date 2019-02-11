package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.services.log_reporter.LogRecordCollector
import org.jetbrains.research.groups.ml_methods.deepbugs.services.log_reporter.createLogRecord
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.EventType
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.events.ThresholdConfigured
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import java.util.*


object DeepBugsJSService {
    const val PLUGIN_NAME = "DeepBugsJavaScript"
    const val PLUGIN_VERSION = "0.1"
    var models = ModelsManager(PLUGIN_NAME)

    private val session = UUID.randomUUID().toString()
    private val logStorage = LogRecordCollector()

    // init {
    //    val client = DownloadClient(PLUGIN_NAME) { DeepBugsJSService.models.initModels() }
    //    client.checkRepos()
    // }

    fun sendInspectionLog(data: BugReport) {
        val toReport = createLogRecord(session, PLUGIN_NAME, PLUGIN_VERSION, EventType.BUG_REPORT, data)
        logStorage.appendReport(toReport)
    }

    fun sendConfigLog(data: ThresholdConfigured) {
        val toReport = createLogRecord(session, PLUGIN_NAME, PLUGIN_VERSION, EventType.THRESHOLD_CONFIGURED, data)
        logStorage.appendReport(toReport)
    }
}