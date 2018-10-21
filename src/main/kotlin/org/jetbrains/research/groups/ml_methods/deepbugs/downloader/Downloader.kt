package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.openapi.application.PathManager
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.Zip
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

data class RepositoryRecord(val target: String, val name: String, val printableName: String)

object Downloader {

    private val modelsPath: String = PathManager.getPluginsPath()
    private fun getRootPath(name: String) = Paths.get(modelsPath, "DeepBugsPlugin", name)
    private fun getTargetPath(target: String, name: String) = Paths.get(modelsPath, "DeepBugsPlugin", target, name)
    private val repositoryFile = getRootPath("repository.json").toFile()
    private var repository: MutableList<RepositoryRecord>

    init {
        if (repositoryFile.exists()) {
            repository = JsonUtils.readCollectionValue(repositoryFile.readText(),
                    MutableList::class as KClass<MutableList<RepositoryRecord>>, RepositoryRecord::class)
        } else {
            repository = ArrayList()
            repositoryFile.parentFile.mkdirs()
            saveRepository()
        }
    }

    fun downloadFile(target: String, name: String, url: String, printableName: String = name): File {
        return repository.firstOrNull { it.name == name && it.target == target }?.let {
            getTargetPath(it.target, it.name).toFile()
        }
                ?: downloadTo(printableName, URL(url), getTargetPath(target, name), DownloadProgressProvider.getProgress()).also {
                    repository.add(RepositoryRecord(target, name, printableName))
                    saveRepository()
                }
    }

    fun downloadZip(target: String, name: String, url: String, printableName: String = name): File {
        return repository.firstOrNull { it.name == name && it.target == target }?.let {
            getTargetPath(it.target, it.name).toFile()
        }
                ?: downloadTo(printableName, URL(url), getTargetPath(target, "$name.zip"), DownloadProgressProvider.getProgress()).let {
                    val zip = Zip.extractFolder(it, getTargetPath(target, "").toFile())
                    Files.deleteIfExists(getTargetPath(target, "$name.zip"))
                    repository.add(RepositoryRecord(target, name, printableName))
                    saveRepository()
                    zip
                }
    }

    private fun downloadTo(printableName: String, url: URL, path: Path, progress: DownloadProgress): File {
        progress.name = printableName
        progress.phase = "Download $printableName"
        progress.progress = 0.0

        path.toFile().parentFile.mkdirs()

        val conn = url.openConnection()
        val size = conn.contentLength

        BufferedInputStream(url.openStream()).use {
            val out = FileOutputStream(path.toFile())
            val data = ByteArray(1024)
            var totalCount = 0
            var count = it.read(data, 0, 1024)
            while (count != -1) {
                out.write(data, 0, count)
                totalCount += count
                progress.progress = if (size == 0) {
                    0.0
                } else {
                    totalCount.toDouble() / size
                }
                count = it.read(data, 0, 1024)
            }
        }
        progress.progress = 1.0
        return path.toFile()
    }

    private fun saveRepository() {
        repositoryFile.writeText(JsonUtils.writeValueAsString(repository))
    }
}