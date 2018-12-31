package org.jetbrains.research.groups.ml_methods.deepbugs.error_reporting

import com.intellij.diagnostic.LogMessageEx
import com.intellij.diagnostic.ReportMessages
import com.intellij.ide.DataManager
import com.intellij.idea.IdeaLogger
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer
import org.jetbrains.research.groups.ml_methods.deepbugs.utils.DeepBugsPluginBundle
import java.awt.Component

class GitHubErrorReporter : ErrorReportSubmitter() {
    override fun submit(events: Array<IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: Consumer<SubmittedReportInfo>): Boolean {
        val errorBean = GitHubErrorBean(events[0].throwable, IdeaLogger.ourLastActionId)
        return doSubmit(events[0], parentComponent, consumer, errorBean, additionalInfo)
    }

    private fun doSubmit(event: IdeaLoggingEvent,
                         parentComponent: Component,
                         callback: Consumer<SubmittedReportInfo>,
                         bean: GitHubErrorBean,
                         description: String?): Boolean {
        val dataContext = DataManager.getInstance().getDataContext(parentComponent)

        bean.description = description
        bean.message = event.message

        val throwable = event.throwable
        if (throwable != null) {
            //TODO Should not be hardcoded
            bean.pluginName = "DeepBugsPlugin"
            bean.pluginVersion = "0.1"
            //val pluginId = IdeErrorsDialog.findPluginId(throwable)
            //if (pluginId != null) {
            //val ideaPluginDescriptor = PluginManager.getPlugin(pluginId)
            //if (ideaPluginDescriptor != null && !ideaPluginDescriptor.isBundled) {
            //bean.pluginName = ideaPluginDescriptor.name
            //bean.pluginVersion = ideaPluginDescriptor.version
            //}
            //}
        }

        val data = event.data

        if (data is LogMessageEx) {
            bean.attachments = data.includedAttachments
        }

        val errorReportInformation = ErrorReportInformation
                .getUsersInformation(bean,
                        ApplicationInfo.getInstance() as ApplicationInfoEx,
                        ApplicationNamesInfo.getInstance())

        val project = CommonDataKeys.PROJECT.getData(dataContext)

        val notifyingCallback = CallbackWithNotification(callback, project)
        val task = AnonymousFeedbackTask(project,
                DeepBugsPluginBundle.message("report.error.progress.dialog.text"),
                true,
                errorReportInformation,
                notifyingCallback)
        if (project == null) {
            task.run(EmptyProgressIndicator())
        } else {
            ProgressManager.getInstance().run(task)
        }
        return true
    }

    override fun getReportActionText(): String =
            DeepBugsPluginBundle.message("report.error.to.plugin.vendor")

    /**
     * Provides functionality to show a error report message to the user that gives a click-able link to the created issue.
     */
    internal class CallbackWithNotification(private val myOriginalConsumer: Consumer<SubmittedReportInfo>, private val myProject: Project?) : Consumer<SubmittedReportInfo> {

        override fun consume(reportInfo: SubmittedReportInfo) {
            myOriginalConsumer.consume(reportInfo)

            if (reportInfo.status == SubmittedReportInfo.SubmissionStatus.FAILED) {
                ReportMessages.GROUP.createNotification(
                        ReportMessages.ERROR_REPORT,
                        reportInfo.linkText,
                        NotificationType.ERROR,
                        NotificationListener.URL_OPENING_LISTENER).setImportant(false).notify(myProject)
            } else {
                ReportMessages.GROUP.createNotification(
                        ReportMessages.ERROR_REPORT,
                        reportInfo.linkText,
                        NotificationType.INFORMATION,
                        NotificationListener.URL_OPENING_LISTENER).setImportant(false).notify(myProject)
            }

        }
    }
}