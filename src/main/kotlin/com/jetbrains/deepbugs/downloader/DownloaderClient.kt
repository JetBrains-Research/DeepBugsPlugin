package com.jetbrains.deepbugs.downloader

import com.jetbrains.deepbugs.downloader.utils.JsonUtils

object DownloaderClient {

    fun downloadModelsAndEmbeddings(configStr: String, progress: DownloadProgress) {
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
}