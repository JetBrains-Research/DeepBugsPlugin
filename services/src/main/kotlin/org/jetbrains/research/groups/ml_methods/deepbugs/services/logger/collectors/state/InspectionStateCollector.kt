package org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.state

import com.intellij.codeInspection.ex.ScopeToolState
import com.intellij.internal.statistic.beans.MetricEvent
import com.intellij.internal.statistic.beans.newMetric
import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.openapi.project.Project
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.logger.collectors.state.project.ProjectStateCollector
import java.util.*

class InspectionStateCollector: ProjectStateCollector {
    override val groupId: String = "inspections"
    override val version: Int = 1

    override fun getStates(project: Project): MutableSet<MetricEvent> {
        val tools = InspectionProjectProfileManager.getInstance(project).currentProfile.allTools
        val result = HashSet<MetricEvent>()
        tools.forEach { tool ->
            when {
                ENABLED(tool)  -> result.add(create(tool, true))
                DISABLED(tool) -> result.add(create(tool, false))
            }
        }
        return result
    }

    private fun create(state: ScopeToolState, enabled: Boolean): MetricEvent {
        val data = FeatureUsageData().addData("enabled", enabled)
        val id = state.tool.id

        return newMetric(id, data)
    }

    companion object {
        private val ENABLED = { state: ScopeToolState ->
            state.isEnabled && state.tool.displayName.contains("DeepBugs")
        }

        private val DISABLED = { state: ScopeToolState ->
            !state.isEnabled && state.tool.displayName.contains("DeepBugs")
        }

    }
}