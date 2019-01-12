package org.jetbrains.research.groups.ml_methods.deepbugs.notifier

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

object DeepBugsNotifier {
    private val commonDeepBugsGroup = NotificationGroup(DeepBugsPluginBundle.message("notification.group.id"),
            NotificationDisplayType.STICKY_BALLOON, true)
    private val notificationTitle = DeepBugsPluginBundle.message("notification.title")

    private fun renderNotificationWithAction(message: String, type: NotificationType, actionText: String, action: () -> Unit) =
            Notification(commonDeepBugsGroup.displayId, notificationTitle, message, type)
                    .addAction(DeepBugsNotificationAction(actionText, action))

    fun notifyWithAction(message: String, type: NotificationType, actionText: String, action: () -> Unit) {
        renderNotificationWithAction(message, type, actionText, action).notify(ProjectManager.getInstance().defaultProject)
    }
}