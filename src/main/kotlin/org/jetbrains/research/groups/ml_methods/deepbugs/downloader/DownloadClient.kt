package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.DeepBugsProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.ModelsHolder

object DownloadClient {

    private fun download(configStr: String, progress: DownloadProgress) {
        val progressFuncLast = DownloadProgressProvider.getProgress
        DownloadProgressProvider.getProgress = { progress }

        val config = JsonUtils.readValue(configStr, Config::class)
        config.classpath.forEach {
            if (it.url.contains(".zip"))
                Downloader.downloadZip(config.name, it.name, it.url)
            else
                Downloader.downloadFile(config.name, it.name, it.url)
        }
        DownloadProgressProvider.getProgress = progressFuncLast
    }

    fun downloadModels() {
        val config = DeepBugsProvider::class.java.classLoader.getResource("models.json").readText()
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject, DeepBugsPluginBundle.message("download.task.title"), true) {
            override fun run(indicator: ProgressIndicator) {
                download(config, DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
            }

            override fun onFinished() {
                ModelsHolder.checkModels()
            }

            override fun onSuccess() {
                Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
                        DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("success.notification.message"),
                        NotificationType.INFORMATION))
            }

            override fun onThrowable(error: Throwable) {
                Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
                        DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("error.notification.message"),
                        NotificationType.ERROR).setImportant(true))
            }

        })
    }
}