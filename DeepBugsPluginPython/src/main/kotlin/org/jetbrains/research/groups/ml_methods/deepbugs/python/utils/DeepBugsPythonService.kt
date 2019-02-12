package org.jetbrains.research.groups.ml_methods.deepbugs.python.utils

import org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader.DownloadClient
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.log_reporter.LogRecordCollector
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.log_reporter.createLogRecord
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.EventType
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.BugReport
import org.jetbrains.research.groups.ml_methods_deepbugs.logger.logging.events.ThresholdConfigured
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import java.util.*

object DeepBugsPythonService {
    const val PY_PLUGIN_NAME = "DeepBugsPython"
    const val PY_PLUGIN_VERSION = "0.1"
    var models = ModelsManager(PY_PLUGIN_NAME)
    private val session = UUID.randomUUID().toString()
    private val logStorage = LogRecordCollector()

    init {
        val client = DownloadClient(PY_PLUGIN_NAME) { DeepBugsPythonService.models.initModels() }
        client.checkRepos()
    }

    fun sendInspectionLog(data: BugReport) {
        val toReport = createLogRecord(session, PY_PLUGIN_NAME, PY_PLUGIN_VERSION, EventType.BUG_REPORT, data)
        logStorage.appendReport(toReport)
    }

    fun sendConfigLog(data: ThresholdConfigured) {
        val toReport = createLogRecord(session, PY_PLUGIN_NAME, PY_PLUGIN_VERSION, EventType.THRESHOLD_CONFIGURED, data)
        logStorage.appendReport(toReport)
    }
}