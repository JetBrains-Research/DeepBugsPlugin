package org.jetbrains.research.deepbugs.common.errors

import com.intellij.diagnostic.*
import com.intellij.ide.DataManager
import com.intellij.ide.plugins.PluginManager
import com.intellij.idea.IdeaLogger
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.*
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.errors.beans.ErrorReport
import org.jetbrains.research.deepbugs.common.errors.beans.GitHubErrorBean
import org.jetbrains.research.deepbugs.common.logger.collectors.counter.ErrorInfoCollector
import java.awt.Component

class GitHubErrorReporter : ErrorReportSubmitter() {
    override fun submit(
        events: Array<IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<SubmittedReportInfo>
    ): Boolean {
        val errorBean = GitHubErrorBean(events[0].throwable, IdeaLogger.ourLastActionId ?: "")
        return doSubmit(events[0], parentComponent, consumer, errorBean, additionalInfo)
    }

    private fun doSubmit(
        event: IdeaLoggingEvent,
        parentComponent: Component,
        callback: Consumer<SubmittedReportInfo>,
        bean: GitHubErrorBean,
        description: String?
    ): Boolean {
        val dataContext = DataManager.getInstance().getDataContext(parentComponent)

        bean.description = description
        bean.message = event.message

        event.throwable?.let {
            val pluginId = IdeErrorsDialog.findPluginId(event.throwable)
            pluginId?.let {
                val ideaPluginDescriptor = PluginManager.getPlugin(pluginId)
                if (ideaPluginDescriptor != null && !ideaPluginDescriptor.isBundled) {
                    bean.pluginName = ideaPluginDescriptor.name ?: "DeepBugsPlugin"
                    bean.pluginVersion = ideaPluginDescriptor.version
                }
            }
        }

        val data = event.data

        if (data is LogMessage) {
            bean.attachments = data.includedAttachments
        }

        val errorReportInformation = ErrorReport.getUsersInformation(bean,
            ApplicationInfo.getInstance() as ApplicationInfoEx,
            ApplicationNamesInfo.getInstance())

        val project = CommonDataKeys.PROJECT.getData(dataContext)

        val notifyingCallback = CallbackWithNotification(callback, project)
        val task = AnonymousFeedBack.SendTask(project, CommonResourceBundle.message("report.error.progress.dialog.text"), true,
            errorReportInformation, notifyingCallback)

        if (project == null) {
            task.run(EmptyProgressIndicator())
        } else {
            ProgressManager.getInstance().run(task)
        }

        ErrorInfoCollector.logInitErrorSubmitted()
        return true
    }

    override fun getReportActionText(): String =
        CommonResourceBundle.message("report.error.to.plugin.vendor")

    /**
     * Provides functionality to show a error report message to the user that gives a click-able link to the created issue.
     */
    internal class CallbackWithNotification(
        private val myOriginalConsumer: Consumer<SubmittedReportInfo>,
        private val myProject: Project?
    ) : Consumer<SubmittedReportInfo> {

        override fun consume(reportInfo: SubmittedReportInfo) {
            myOriginalConsumer.consume(reportInfo)

            when (reportInfo.status) {
                SubmittedReportInfo.SubmissionStatus.FAILED -> {
                    ReportMessages.GROUP.createNotification(
                        ReportMessages.ERROR_REPORT,
                        reportInfo.linkText,
                        NotificationType.ERROR,
                        NotificationListener.URL_OPENING_LISTENER).setImportant(false).notify(myProject)
                }
                else -> {
                    ReportMessages.GROUP.createNotification(
                        ReportMessages.ERROR_REPORT,
                        reportInfo.linkText,
                        NotificationType.INFORMATION,
                        NotificationListener.URL_OPENING_LISTENER).setImportant(false).notify(myProject)
                }
            }
        }
    }
}
