package org.jetbrains.research.deepbugs.common.settings

import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.deepbugs.common.msgbus.DeepBugsToolLifecycle

interface DeepBugsInspectionConfig {
    val configId: String
    var curState: DeepBugsState

    fun update(newState: DeepBugsState) {
        val prevState = curState
        curState = newState

        ProjectManager.getInstance().openProjects.forEach {
            DeepBugsToolLifecycle.publisher.updateState(prevState, curState, it)
        }
    }
}
