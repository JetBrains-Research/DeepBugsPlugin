package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import org.jetbrains.research.groups.ml_methods.deepbugs.DeepBugsProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

object DownloadHelper {

    private fun download(configStr: String, progress: DownloadProgress) : Boolean {
        val progressFuncLast = DownloadProgressProvider.getProgress
        DownloadProgressProvider.getProgress = { progress }

        val config = JsonUtils.readValue(configStr, Config::class)
        config.classpath.forEach{
            if (it.url.contains(".zip"))
                Downloader.downloadZip(config.name, it.name, it.url) ?: return false
            else
                Downloader.downloadFile(config.name, it.name, it.url) ?: return false
        }
        DownloadProgressProvider.getProgress = progressFuncLast
        return true
    }

    fun downloadModelsAndEmbeddings() : Boolean {
        var success = false
        ProgressManager.getInstance().run(object : Task.Modal(null, DeepBugsPluginBundle.message("download.task.title"), false) {
            override fun run(indicator: ProgressIndicator) {
                val config = DeepBugsProvider::class.java.classLoader.getResource("config.json").readText()
                runReadAction {
                    if (download(config, DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator)))
                        success = true
                }
            }
        })
        return success
    }
}