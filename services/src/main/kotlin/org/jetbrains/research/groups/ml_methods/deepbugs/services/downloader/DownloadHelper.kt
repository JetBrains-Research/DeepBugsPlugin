package org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader

import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.JsonUtils
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

class DownloadHelper(private val pluginRoot: String) {
    private val config = DownloadHelper::class.java.classLoader.getResource("models.json").readText()
    private val remoteRepo = JsonUtils.readValue(config, Config::class)

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
        val downloader = Downloader(pluginRoot)
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
}