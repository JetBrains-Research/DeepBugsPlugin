package org.jetbrains.research.deepbugs.common.ide.fus.collectors.state

import com.intellij.internal.statistic.beans.MetricEvent
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project

interface ProjectStateCollector {
    val groupId: String
    val version: Int

    fun getStates(project: Project): Set<MetricEvent>
}
