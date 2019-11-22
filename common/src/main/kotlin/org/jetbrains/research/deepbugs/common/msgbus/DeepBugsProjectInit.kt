package org.jetbrains.research.deepbugs.common.msgbus

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.deepbugs.common.settings.DeepBugsState

class DeepBugsProjectInit : DumbAware, StartupActivity {
    override fun runActivity(project: Project) {
        DeepBugsToolLifecycle.publisher.initTracking(DeepBugsState(), project)
    }
}
