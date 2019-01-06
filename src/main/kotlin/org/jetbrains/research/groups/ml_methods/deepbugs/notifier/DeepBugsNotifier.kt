package org.jetbrains.research.groups.ml_methods.deepbugs.notifier

import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle


class DeepBugsNotifier {
    private val commonDeepBugsGroup = NotificationGroup(DeepBugsPluginBundle.message("notification.group.id"),
            NotificationDisplayType.STICKY_BALLOON, true)
    private val notificationTitle = DeepBugsPluginBundle.message("notification.title")

    private fun renderNotificationWithAction(message: String, type: NotificationType, actionText: String, action: () -> Unit) =
            Notification(commonDeepBugsGroup.displayId, notificationTitle, message, type)
                    .addAction(object : DumbAwareAction(actionText) {
                        override fun actionPerformed(e: AnActionEvent) {
                            val model = EventLog.getLogModel(e.project)
                            model.notifications.firstOrNull { it.content == message }?.let { notification ->
                                model.removeNotification(notification)
                                notification.expire()
                            }
                            action.invoke()
                        }

                        override fun update(e: AnActionEvent) {
                            val project = e.project ?: return
                            e.presentation.isEnabled = !EventLog.getLogModel(project).notifications.isEmpty()
                        }
                    })

    fun notifyWithAction(message: String, type: NotificationType, actionText: String, action: () -> Unit) {
        renderNotificationWithAction(message, type, actionText, action).notify(ProjectManager.getInstance().defaultProject)
    }
}