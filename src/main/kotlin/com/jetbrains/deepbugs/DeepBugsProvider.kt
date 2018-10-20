package com.jetbrains.deepbugs

import com.intellij.codeInspection.InspectionToolProvider
import com.jetbrains.deepbugs.downloader.DownloaderClient
import com.jetbrains.deepbugs.downloader.SimpleDownloadProgress

class DeepBugsProvider : InspectionToolProvider {
    init {
        val configStr = DeepBugsProvider::class.java.classLoader.getResource("config.json").readText()
        DownloaderClient.downloadModelsAndEmbeddings(configStr, SimpleDownloadProgress())
    }
    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(DeepBugsInspection::class.java)
    }
}