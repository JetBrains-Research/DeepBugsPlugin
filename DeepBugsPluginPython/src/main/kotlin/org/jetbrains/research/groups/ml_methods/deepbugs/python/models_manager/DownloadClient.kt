package org.jetbrains.research.groups.ml_methods.deepbugs.python.models_manager

import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.python.utils.DeepBugsPythonBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader.DownloadHelper
import org.jetbrains.research.groups.ml_methods.deepbugs.services.notifier.DeepBugsNotifier
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import java.nio.file.Paths

object DownloadClient {
    private val root = Paths.get(PathManager.getPluginsPath(), ModelsManager.pluginName).toString()
    private val client = DownloadHelper(root)

    fun checkRepos() {
        when (client.modelFilesExists()) {
            true -> ModelsManager.initModels()
            false -> DeepBugsNotifier.notifyWithAction(DeepBugsPythonBundle.message("notification.title"), DeepBugsPluginServicesBundle.message("download.notification.message"),
                    NotificationType.INFORMATION, DeepBugsPluginServicesBundle.message("download.text"), DownloadClient::downloadAndInitModels)
        }
    }

    fun downloadAndInitModels() {
        val downloadTask = DownloadTask(ProjectManager.getInstance().defaultProject,
                DeepBugsPythonBundle.message("download.task.title"), true, client::download)
        ProgressManager.getInstance().run(downloadTask)
    }
}