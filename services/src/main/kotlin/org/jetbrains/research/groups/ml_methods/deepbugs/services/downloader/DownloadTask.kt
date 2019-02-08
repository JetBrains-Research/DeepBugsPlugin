package org.jetbrains.research.groups.ml_methods.deepbugs.services.downloader

import com.intellij.notification.NotificationType
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import org.jetbrains.research.groups.ml_methods.deepbugs.services.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.services.notifier.DeepBugsNotifier
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.DeepBugsPluginServicesBundle
import org.jetbrains.research.groups.ml_methods.deepbugs.services.utils.PlatformUtils

class DownloadTask(project: Project?, message: String, canBeCancelled: Boolean,
        private val toRun: (DownloadProgress) -> Unit) : Task.Backgroundable(project, message, canBeCancelled) {

    override fun run(indicator: ProgressIndicator) {
        toRun.invoke(DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
    }

    override fun onSuccess() {
        ModelsManager.initModels()
    }

    override fun onThrowable(error: Throwable) {
        DeepBugsNotifier.notifyWithAction("<b>${PlatformUtils.getPluginName()}</b>",
                DeepBugsPluginServicesBundle.message("error.notification.message"),
                NotificationType.ERROR, DeepBugsPluginServicesBundle.message("restart.download.text"), DownloadClient::downloadAndInitModels)
    }

    override fun onCancel() {
        DeepBugsNotifier.notifyWithAction("<b>${PlatformUtils.getPluginName()}</b>",
                DeepBugsPluginServicesBundle.message("cancel.notification.message"),
                NotificationType.WARNING, DeepBugsPluginServicesBundle.message("restart.download.text"), DownloadClient::downloadAndInitModels)
    }
}