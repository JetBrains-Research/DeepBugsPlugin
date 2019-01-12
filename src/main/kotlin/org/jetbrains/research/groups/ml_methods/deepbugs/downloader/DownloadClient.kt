package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.notifier.DeepBugsNotifier
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

object DownloadClient {

    private val root = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin").toString()
    private val config = DownloadClient::class.java.classLoader.getResource("models.json").readText()
    private val remoteRepo = JsonUtils.readValue(config, Config::class)

    fun checkRepos() {
        when (modelFilesExists()) {
            true -> ModelsManager.initModels()
            false -> DeepBugsNotifier.notifyWithAction(DeepBugsPluginBundle.message("download.notification.message"),
                    NotificationType.INFORMATION, DeepBugsPluginBundle.message("download.text"), DownloadClient::downloadAndInitModels)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun modelFilesExists(): Boolean {
        val localRepoPath = Paths.get(root, "repository.json")
        if (!Files.exists(localRepoPath))
            return false
        val localRepo = JsonUtils.readCollectionValue(localRepoPath.toFile().readText(),
                MutableList::class as KClass<MutableList<RepositoryRecord>>, RepositoryRecord::class)
        remoteRepo.classpath.forEach { record ->
            localRepo.firstOrNull { it.name == record.name } ?: return false
        }
        return true
    }

    private fun download(progress: DownloadProgress) {
        val progressFuncLast = DownloadProgressProvider.getProgress
        DownloadProgressProvider.getProgress = { progress }
        remoteRepo.classpath.forEach {
            if (it.url.contains(".zip")) {
                Downloader.downloadZip(remoteRepo.name, it.name, it.url)
            } else
                Downloader.downloadFile(remoteRepo.name, it.name, it.url)
        }
        DownloadProgressProvider.getProgress = progressFuncLast
    }

    private fun downloadAndInitModels() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginBundle.message("download.task.title"), true) {
            override fun run(indicator: ProgressIndicator) {
                download(DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
            }

            override fun onFinished() {
                ModelsManager.initModels()
            }

            //TODO: downloadModels inside task?? meh.
            override fun onThrowable(error: Throwable) {
                DeepBugsNotifier.notifyWithAction(DeepBugsPluginBundle.message("error.notification.message"),
                        NotificationType.ERROR, DeepBugsPluginBundle.message("restart.download.text"), DownloadClient::downloadAndInitModels)
            }

            override fun onCancel() {
                DeepBugsNotifier.notifyWithAction(DeepBugsPluginBundle.message("cancel.notification.message"),
                        NotificationType.WARNING, DeepBugsPluginBundle.message("restart.download.text"), DownloadClient::downloadAndInitModels)
            }
        })
    }
}