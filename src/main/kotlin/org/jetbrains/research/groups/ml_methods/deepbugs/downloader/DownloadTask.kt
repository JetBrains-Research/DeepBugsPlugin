package org.jetbrains.research.groups.ml_methods.deepbugs.downloader

import com.intellij.notification.NotificationType
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import org.jetbrains.research.groups.ml_methods.deepbugs.models_manager.ModelsManager
import org.jetbrains.research.groups.ml_methods.deepbugs.notifier.DeepBugsNotifier
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle

class DownloadTask(project: Project?, message: String, canBeCancelled: Boolean,
        private val toRun: (DownloadProgress) -> Unit) : Backgroundable(project, message, canBeCancelled) {

    override fun run(indicator: ProgressIndicator) {
        toRun.invoke(DownloadProgressWrapper(ProgressManager.getInstance().progressIndicator))
    }

    override fun onSuccess() {
        ModelsManager.initModels()
    }

    override fun onThrowable(error: Throwable) {
        DeepBugsNotifier.notifyWithAction(DeepBugsPluginBundle.message("error.notification.message"),
                NotificationType.ERROR, DeepBugsPluginBundle.message("restart.download.text"), DownloadClient::downloadAndInitModels)
    }

    override fun onCancel() {
        DeepBugsNotifier.notifyWithAction(DeepBugsPluginBundle.message("cancel.notification.message"),
                NotificationType.WARNING, DeepBugsPluginBundle.message("restart.download.text"), DownloadClient::downloadAndInitModels)
    }
}