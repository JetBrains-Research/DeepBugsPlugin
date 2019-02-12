package org.jetbrains.research.groups.ml_methods.deepbugs.services.notifier

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsServicesBundle

object DeepBugsNotifier {
    private val commonDeepBugsGroup = NotificationGroup(DeepBugsServicesBundle.message("notification.group.id"),
            NotificationDisplayType.STICKY_BALLOON, true)

    private fun renderNotificationWithAction(notificationTitle: String, message: String, type: NotificationType, actionText: String, action: () -> Unit) =
            Notification(commonDeepBugsGroup.displayId, notificationTitle, message, type)
                    .addAction(DeepBugsNotificationAction(actionText, action))

    fun notifyWithAction(notificationTitle: String, message: String, type: NotificationType, actionText: String, action: () -> Unit) {
        renderNotificationWithAction(notificationTitle, message, type, actionText, action).notify(ProjectManager.getInstance().defaultProject)
    }
}