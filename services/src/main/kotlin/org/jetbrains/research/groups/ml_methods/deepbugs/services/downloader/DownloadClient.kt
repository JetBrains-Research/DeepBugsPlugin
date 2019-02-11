package org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader

import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.notifier.DeepBugsNotifier
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.JsonUtils
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

class DownloadClient(private val pluginName: String, private val afterDownload: () -> Unit) {
    private val pluginRoot = Paths.get(PathManager.getPluginsPath(), pluginName).toString()
    private val config = DownloadClient::class.java.classLoader.getResource("models.json").readText()
    private val remoteRepo = JsonUtils.readValue(config, Config::class)
    private val downloader = Downloader(pluginName)

    fun checkRepos() {
        when (modelFilesExists()) {
            true -> afterDownload.invoke()
            false -> DeepBugsNotifier.notifyWithAction("<b>$pluginName</b>", DeepBugsPluginServicesBundle.message("download.notification.message"),
                    NotificationType.INFORMATION, DeepBugsPluginServicesBundle.message("download.text"), ::downloadAndInitModels)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun modelFilesExists(): Boolean {
        val localRepoPath = Paths.get(pluginRoot, "repository.json")
        if (!Files.exists(localRepoPath))
            return false
        val localRepo = JsonUtils.readCollectionValue(localRepoPath.toFile().readText(),
                MutableList::class as KClass<MutableList<RepositoryRecord>>, RepositoryRecord::class)
        remoteRepo.classpath.forEach { record ->
            localRepo.firstOrNull { it.name == record.name } ?: return false
        }
        return true
    }

    fun download(progress: DownloadProgress) {
        val progressFuncLast = DownloadProgressProvider.getProgress
        DownloadProgressProvider.getProgress = { progress }
        remoteRepo.classpath.forEach {
            if (it.url.contains(".zip")) {
                downloader.downloadZip(remoteRepo.name, it.name, it.url)
            } else
                downloader.downloadFile(remoteRepo.name, it.name, it.url)
        }
        DownloadProgressProvider.getProgress = progressFuncLast
    }

    fun downloadAndInitModels() {
        val downloadTask = DownloadTask(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginServicesBundle.message("download.task.title", pluginName), true)
        ProgressManager.getInstance().run(downloadTask)
    }

    private inner class DownloadTask(project: Project?, message: String, canBeCancelled: Boolean) : Task.Backgroundable(project, message, canBeCancelled) {

        override fun run(indicator: ProgressIndicator) {
            download(DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
        }

        override fun onSuccess() {
            afterDownload.invoke()
        }

        override fun onThrowable(error: Throwable) {
            DeepBugsNotifier.notifyWithAction("<b>$pluginName</b>",
                    DeepBugsPluginServicesBundle.message("error.notification.message"),
                    NotificationType.ERROR, DeepBugsPluginServicesBundle.message("restart.download.text"), ::downloadAndInitModels)
        }

        override fun onCancel() {
            DeepBugsNotifier.notifyWithAction("<b>$pluginName</b>",
                    DeepBugsPluginServicesBundle.message("cancel.notification.message"),
                    NotificationType.WARNING, DeepBugsPluginServicesBundle.message("restart.download.text"), ::downloadAndInitModels)
        }
    }
}