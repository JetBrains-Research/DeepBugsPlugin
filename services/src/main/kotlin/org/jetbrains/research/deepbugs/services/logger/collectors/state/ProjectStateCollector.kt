package org.jetbrains.research.deepbugs.services.logger.collectors.state

import com.intellij.internal.statistic.beans.MetricEvent
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project

interface ProjectStateCollector {
    val groupId: String
    val version: Int

    fun getStates(project: Project): Set<MetricEvent>

    companion object {
        private val EP_NAME = ExtensionPointName.create<ProjectStateCollector>("dbp.statistics.projectStateCollector")

        fun getExtensions(): Set<ProjectStateCollector> {
            return EP_NAME.extensionList.toSet()
        }
    }
}
