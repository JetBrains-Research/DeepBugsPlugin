package org.jetbrains.research.groups.ml_methods.deepbugs.notifier

import com.intellij.notification.EventLog
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

class DeepBugsNotificationAction(actionText: String = "", private val action: () -> Unit = {})
    : DumbAwareAction(actionText) {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = !EventLog.getLogModel(e.project).notifications.isEmpty()
    }

    override fun actionPerformed(e: AnActionEvent) {
        EventLog.getLogModel(e.project).let { model ->
            model.notifications.firstOrNull {
                it.groupId == DeepBugsPluginBundle.message("notification.group.id")
            }?.let { notification ->
                model.removeNotification(notification)
                notification.expire()
            }
        }
        action.invoke()
    }
}