package org.jetbrains.research.deepbugs.common.ide.msg

import com.intellij.application.subscribe
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.messages.Topic
import org.jetbrains.research.deepbugs.common.DeepBugsConfig

interface DeepBugsLifecycle {
    companion object {
        private val topic = Topic.create("deepbugs_lifecycle", DeepBugsLifecycle::class.java)
        val publisher by lazy { ApplicationManager.getApplication().messageBus.syncPublisher(topic) }

        init {
            topic.subscribe(ApplicationManager.getApplication(), InspectionStateHandler)
        }
    }

    fun init(init: DeepBugsConfig.State)

    fun update(previous: DeepBugsConfig.State, new: DeepBugsConfig.State)
}
