package org.jetbrains.research.groups.ml_methods.deepbugs

import com.intellij.codeInspection.InspectionToolProvider
import com.intellij.notification.*
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.DownloaderClient
import org.jetbrains.research.groups.ml_methods.deepbugs.downloader.SimpleDownloadProgress
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsBinOperandInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.inspections.DeepBugsBinOperatorInspection
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import java.net.UnknownHostException

fun showErrorNotification(){
    Notifications.Bus.register(DeepBugsPluginBundle.message("notification.group.id"), NotificationDisplayType.STICKY_BALLOON)
    Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("notification.group.id"),
            DeepBugsPluginBundle.message("notification.title"), DeepBugsPluginBundle.message("notification.message"),
            NotificationType.ERROR).setImportant(true))
}
class DeepBugsProvider : InspectionToolProvider {
    init {
        val configStr = DeepBugsProvider::class.java.classLoader.getResource("config.json").readText()
        try {
            DownloaderClient.downloadModelsAndEmbeddings(configStr, SimpleDownloadProgress())
        }
        catch (ex: UnknownHostException) {
            showErrorNotification()
        }
    }

    override fun getInspectionClasses(): Array<Class<*>> {
        return arrayOf(DeepBugsBinOperatorInspection::class.java, DeepBugsBinOperandInspection::class.java)
    }
}