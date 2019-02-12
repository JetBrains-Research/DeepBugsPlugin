package org.jetbrains.research.groups.ml_methods.deepbugs.javascript.utils

import org.jetbrains.research.groups.ml_methods_deepbugs.logger.log_reporter.LogRecordCollector
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.log_reporter.createLogRecord
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.EventType
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.ThresholdConfigured
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import java.util.*


object DeepBugsJSService {
    const val JS_PLUGIN_NAME = "DeepBugsJavaScript"
    const val JS_PLUGIN_VERSION = "0.1"
    var models = ModelsManager(JS_PLUGIN_NAME)

    private val session = UUID.randomUUID().toString()
    private val logStorage = LogRecordCollector()

    //TODO: add model files
    // init {
    //    val client = DownloadClient(JS_PLUGIN_NAME) { DeepBugsJSService.models.initModels() }
    //    client.checkRepos()
    // }

    fun sendInspectionLog(data: BugReport) {
        val toReport = createLogRecord(session, JS_PLUGIN_NAME, JS_PLUGIN_VERSION, EventType.BUG_REPORT, data)
        logStorage.appendReport(toReport)
    }

    fun sendConfigLog(data: ThresholdConfigured) {
        val toReport = createLogRecord(session, JS_PLUGIN_NAME, JS_PLUGIN_VERSION, EventType.THRESHOLD_CONFIGURED, data)
        logStorage.appendReport(toReport)
    }
}