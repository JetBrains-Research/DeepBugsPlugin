package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.runModalTask
import org.jetbrains.research.groups.ml_methods.deepbugs.DeepBugsProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.utils.JsonUtils
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

object DownloadHelper {

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
        runModalTask(DeepBugsPluginBundle.message("download.task.title"), null, true,
                { download(config, DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator)) } )
    }
}