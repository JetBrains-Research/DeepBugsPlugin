package org.jetbrains.research.groups.ml_methods.deepbugs

import com.intellij.codeInspection.InspectionToolProvider
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloaderClient
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.SimpleDownloadProgress

class DeepBugsProvider : InspectionToolProvider {
    //init {
        //val configStr = DeepBugsProvider::class.java.classLoader.getResource("config.json").readText()
        //DownloaderClient.downloadModelsAndEmbeddings(configStr, SimpleDownloadProgress())
    //}
    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(DeepBugsBinOperatorInspection::class.java)
    }
}