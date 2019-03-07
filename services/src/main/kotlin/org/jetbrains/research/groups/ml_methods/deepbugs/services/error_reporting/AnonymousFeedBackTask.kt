package org.jetbrains.research.groups.ml_methods.deepbugs.services.error_reporting

import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer


/**
 * Encapsulates the sending of feedback into a background task that is run by [GitHubErrorReporter]
 */
class AnonymousFeedbackTask internal constructor(
        project: Project?,
        title: String,
        canBeCancelled: Boolean,
        private val errorReportInformation: ErrorReportInformation,
        private val myCallback: Consumer<SubmittedReportInfo>
) : Backgroundable(project, title, canBeCancelled) {

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true
        myCallback.consume(AnonymousFeedback.sendFeedback(errorReportInformation))
    }
}