package org.jetbrains.research.deepbugs.common.msgbus

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.messages.Topic
import org.jetbrains.research.deepbugs.common.settings.DeepBugsState

interface DeepBugsToolLifecycle {
    fun initTracking(state: DeepBugsState, project: Project) {
        ProjectManager.getInstance().openProjects.forEach {
            DaemonCodeAnalyzer.getInstance(it).restart()
        }
    }

    fun updateState(prevState: DeepBugsState, newState: DeepBugsState, project: Project) {
        if (prevState != newState) initTracking(newState, project)
    }

    companion object {
        val targetTopic = Topic.create("deepbugs_config_topic", DeepBugsToolLifecycle::class.java)
        val publisher by lazy { ApplicationManager.getApplication().messageBus.syncPublisher(targetTopic) }
    }
}
