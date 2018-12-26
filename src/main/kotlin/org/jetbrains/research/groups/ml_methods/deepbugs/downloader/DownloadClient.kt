package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.notification.*
import com.intellij.openapi.progress.*
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.DeepBugsProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.ModelsHolder
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

object DownloadClient {

    private fun download(configStr: String, progress: DownloadProgress) {
        val progressFuncLast = DownloadProgressProvider.getProgress
        DownloadProgressProvider.getProgress = { progress }

        val config = JsonUtils.readValue(configStr, Config::class)
        config.classpath.forEach{
            if (it.url.contains(".zip"))
                Downloader.downloadZip(config.name, it.name, it.url)
            else
                Downloader.downloadFile(config.name, it.name, it.url)
        }
        DownloadProgressProvider.getProgress = progressFuncLast
    }

    fun downloadModelsAndEmbeddings() {
        val config = DeepBugsProvider::class.java.classLoader.getResource("models.json").readText()
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject, "Download models and embeddings", true) {
            override fun run(indicator: ProgressIndicator) {
                download(config, DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
            }

            override fun onFinished() {
                ModelsHolder.checkModels()
            }

            override fun onSuccess() {
                Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
                        DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("success.notification.message"),
                        NotificationType.INFORMATION).setImportant(true))
            }

            override fun onThrowable(error: Throwable) {
                Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
                        DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("error.notification.message"),
                        NotificationType.ERROR).setImportant(true))
            }

        })
    }
}