package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.notification.*
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.DeepBugsProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import java.nio.file.Files
import javax.swing.event.HyperlinkEvent

object DownloadClient {

    init {
        NotificationGroup(DeepBugsPluginBundle.message("success.notification.group.id"), NotificationDisplayType.BALLOON, false)
        NotificationGroup(DeepBugsPluginBundle.message("error.notification.group.id"), NotificationDisplayType.STICKY_BALLOON, true)
        NotificationGroup(DeepBugsPluginBundle.message("notification.group.id"), NotificationDisplayType.STICKY_BALLOON, true)
    }

    fun showDownloadNotification() {
        Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
                DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("download.notification.message"),
                NotificationType.INFORMATION, object : NotificationListener.Adapter() {
            override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                downloadModels()
                notification.expire()
            }
        }).setImportant(true))
    }

    private fun showSuccessNotification() {
        Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("success.notification.group.id"),
                DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("success.notification.message"),
                NotificationType.INFORMATION))
    }

    private fun showErrorNotification() {
        Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("error.notification.group.id"),
                DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("error.notification.message"),
                NotificationType.ERROR, object : NotificationListener.Adapter() {
            override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                downloadModels()
                notification.expire()
            }
        }).setImportant(true))
    }

    private fun showCancelNotification() {
        Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
                DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("cancel.notification.message"),
                NotificationType.WARNING, object : NotificationListener.Adapter() {
            override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                downloadModels()
                notification.expire()
            }
        }).setImportant(true))
    }

    private fun download(configStr: String, progress: DownloadProgress) {
        val progressFuncLast = DownloadProgressProvider.getProgress
        DownloadProgressProvider.getProgress = { progress }

        val config = JsonUtils.readValue(configStr, Config::class)
        config.classpath.forEach {
            if (it.url.contains(".zip")) {
                Downloader.downloadZip(config.name, it.name, it.url)
            } else
                Downloader.downloadFile(config.name, it.name, it.url)
        }
        DownloadProgressProvider.getProgress = progressFuncLast
    }

    private fun downloadModels() {
        val config = DeepBugsProvider::class.java.classLoader.getResource("models.json").readText()
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginBundle.message("download.task.title"), true) {
            override fun run(indicator: ProgressIndicator) {
                download(config, DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
            }

            override fun onFinished() {
                ModelsManager.initModels()
            }

            override fun onSuccess() {
                showSuccessNotification()
            }

            override fun onThrowable(error: Throwable) {
                showErrorNotification()
            }

            override fun onCancel() {
                showCancelNotification()
            }
        })
    }


}