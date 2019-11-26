package org.jetbrains.research.deepbugs.common.ide.fus.collectors.state

import com.intellij.codeInspection.ex.ScopeToolState
import com.intellij.internal.statistic.beans.MetricEvent
import com.intellij.internal.statistic.beans.newMetric
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.project.Project
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import java.lang.IllegalStateException

import java.util.*

class InspectionStateCollector : ProjectStateCollector {
    override val groupId: String = "dbp.inspections"
    override val version: Int = 1

    private val langPrefix: String
        get() = when {
            isPython() -> "Py"
            isJavaScript() -> "JS"
            else -> throw IllegalStateException("Unsupported language")
        }

    private fun isPython() = DeepBugsPlugin.dependentPlugins.any { it.idString.contains("com.intellij.modules.python")}
    private fun isJavaScript() = DeepBugsPlugin.dependentPlugins.any { it.idString.contains("JavaScript") }

    override fun getStates(project: Project): MutableSet<MetricEvent> {
        val tools = InspectionProjectProfileManager.getInstance(project).currentProfile.allTools
        val deepBugsTools = tools.filter {
            IS_DEEP_BUGS(it) && it.tool.shortName.startsWith(langPrefix)
        }
        val result = HashSet<MetricEvent>()
        deepBugsTools.forEach { tool ->
            when {
                ENABLED(tool) -> result.add(createInspectionStateMetric(tool, true))
                DISABLED(tool) -> result.add(createInspectionStateMetric(tool, false))
            }
        }
        return result
    }

    private fun createInspectionStateMetric(state: ScopeToolState, enabled: Boolean): MetricEvent {
        val inspection = state.tool.shortName
        val data = FeatureUsageData()
            .addData("inspection", inspection)
            .addData("enabled", enabled)
            .addData("not_default_state", NOT_DEFAULT(state, enabled))

        return newMetric(INSPECTION_STATUS_EVENT, data)
    }

    companion object {
        private const val INSPECTION_STATUS_EVENT = "inspection.status"

        private val ENABLED = { state: ScopeToolState -> state.isEnabled }

        private val DISABLED = { state: ScopeToolState -> !state.isEnabled }

        private val NOT_DEFAULT = { state: ScopeToolState, enabled: Boolean ->
            enabled != state.tool.isEnabledByDefault
        }

        private val IS_DEEP_BUGS = { state: ScopeToolState -> state.tool.displayName.contains("DeepBugs") }
    }
}
