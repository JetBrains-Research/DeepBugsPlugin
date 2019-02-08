package org.jetbrains.research.groups.ml_methods.deepbugs.python.utils

import com.intellij.ide.plugins.PluginManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.datatypes.DataType
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.BugFeatures
import org.jetbrains.research.groups.ml_methods.deepbugs.services.log_reporter.DeepBugsLogReporter
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logging.ThresholdConfigFeatures
import java.util.*

object ReportingUtils {
    private val reporter = DeepBugsLogReporter()
    private val pluginName = DeepBugsPythonBundle.message("plugin.name")

    fun sendInspectionLog(session: UUID, codePiece: DataType, bugName: String, probability: Float, curThreshold: Float) {
        codePiece::class.simpleName?.let{ codePieceType ->
            val toReport = BugFeatures(codePieceType, bugName, probability, curThreshold)
            reporter.log(session, toReport, pluginName)
        }
    }

    fun sendConfigLog(session: UUID, features: ThresholdConfigFeatures){
        reporter.log(session, features, pluginName)
    }
}