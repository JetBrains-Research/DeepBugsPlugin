package org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader

import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.notifier.DeepBugsNotifier
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.PlatformUtils
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

object DownloadClient {
    private val pluginName = PlatformUtils.getPluginName()
    private val pluginRoot = Paths.get(PathManager.getPluginsPath(), pluginName).toString()
    private val config = DownloadClient::class.java.classLoader.getResource("models.json").readText()
    private val remoteRepo = JsonUtils.readValue(config, Config::class)

    fun checkRepos() {
        when (modelFilesExists()) {
            true -> ModelsManager.initModels()
            false -> DeepBugsNotifier.notifyWithAction("<b>$pluginName</b>", DeepBugsPluginServicesBundle.message("download.notification.message"),
                    NotificationType.INFORMATION, DeepBugsPluginServicesBundle.message("download.text"), DownloadClient::downloadAndInitModels)
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
                Downloader.downloadZip(remoteRepo.name, it.name, it.url)
            } else
                Downloader.downloadFile(remoteRepo.name, it.name, it.url)
        }
        DownloadProgressProvider.getProgress = progressFuncLast
    }

    fun downloadAndInitModels() {
        val downloadTask = DownloadTask(ProjectManager.getInstance().defaultProject,
                DeepBugsPluginServicesBundle.message("download.task.title", pluginName), true, ::download)
        ProgressManager.getInstance().run(downloadTask)
    }
}