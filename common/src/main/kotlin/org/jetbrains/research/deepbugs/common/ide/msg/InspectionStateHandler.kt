package org.jetbrains.research.deepbugs.common.ide.msg

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

object InspectionStateHandler : DeepBugsLifecycle {
    override fun init(init: DeepBugsConfig.State) {
        ProjectManager.getInstance().openProjects.forEach {
            DaemonCodeAnalyzer.getInstance(it).restart()
        }
    }

    override fun update(previous: DeepBugsConfig.State, new: DeepBugsConfig.State) {
        if (previous == new) return
        init(new)
    }
}
