package org.jetbrains.research.deepbugs.common.utils.errors

import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer
import org.eclipse.egit.github.core.*
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.IssueService
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.utils.errors.beans.ErrorReport
import org.jetbrains.research.deepbugs.common.utils.errors.github.GitHubTokenScrambler

/**
 * Provides functionality to create and send GitHub issues when an exception is thrown by a plugin.
 */
internal object AnonymousFeedBack {
    private const val TOKEN_FILE = "errorReporterToken.dms"
    private const val GIT_REPO_USER = "JetBrains-Research"
    private const val GIT_REPO = "DeepBugsPlugin"
    private const val ISSUE_LABEL_BUG = "bug"
    private const val ISSUE_LABEL_AUTO_GENERATED = "auto-generated"
    private const val GIT_ISSUE_TITLE = "[auto-generated:%s] %s"
    private const val HTML_URL_TO_CREATE_NEW_ISSUE = "https://github.com/JetBrains-Research/DeepBugsPlugin/issues/new"

    /**
     * Encapsulates the sending of feedback into a background task that is run by [GitHubErrorReporter]
     */
    class SendTask(project: Project?, title: String, canBeCancelled: Boolean,
                   private val errorReportInformation: ErrorReport,
                   private val myCallback: Consumer<SubmittedReportInfo>) : Task.Backgroundable(project, title, canBeCancelled) {
        override fun run(indicator: ProgressIndicator) {
            indicator.isIndeterminate = true
            myCallback.consume(sendFeedback(errorReportInformation))
        }
    }

    /**
     * Makes a connection to GitHub. Checks if there is an issue that is a duplicate and based on this, creates either a
     * new issue or comments on the duplicate (if the user provided additional information).
     *
     * @param errorReportInformation Information collected by [ErrorReport]
     * @return The report info that is then used in [GitHubErrorReporter] to show the user a balloon with the link
     * of the created issue.
     */
    fun sendFeedback(errorReportInformation: ErrorReport): SubmittedReportInfo {
        return try {
            val tokenFile = AnonymousFeedBack::class.java.classLoader.getResourceAsStream(TOKEN_FILE)!!
            val gitAccessToken = GitHubTokenScrambler.decrypt(tokenFile)

            val client = GitHubClient()
            client.setOAuth2Token(gitAccessToken)
            val repoID = RepositoryId(GIT_REPO_USER, GIT_REPO)
            val issueService = IssueService(client)

            var newGibHubIssue = createNewGibHubIssue(errorReportInformation)
            val duplicate = findFirstDuplicate(newGibHubIssue.title, issueService, repoID)
            val isNewIssue = duplicate == null
            newGibHubIssue = if (!isNewIssue) {
                val newErrorComment = generateGitHubIssueBody(errorReportInformation, false)
                issueService.createComment(repoID, duplicate!!.number, newErrorComment)
                duplicate
            } else {
                issueService.createIssue(repoID, newGibHubIssue)
            }

            val message = CommonResourceBundle.message(
                if (isNewIssue) "git.issue.text" else "git.issue.duplicate.text",
                newGibHubIssue.htmlUrl,
                newGibHubIssue.number
            )
            SubmittedReportInfo(newGibHubIssue.htmlUrl, message, if (isNewIssue) SubmissionStatus.NEW_ISSUE else SubmissionStatus.DUPLICATE)
        } catch (e: Exception) {
            SubmittedReportInfo(
                HTML_URL_TO_CREATE_NEW_ISSUE,
                CommonResourceBundle.message("report.error.connection.failure", HTML_URL_TO_CREATE_NEW_ISSUE),
                SubmissionStatus.FAILED
            )
        }

    }

    /**
     * Collects all issues on the repo and finds the first duplicate that has the same title. For this to work, the title
     * contains the hash of the stack trace.
     *
     * @param uniqueTitle Title of the newly created issue. Since for auto-reported issues the title is always the same,
     * it includes the hash of the stack trace. The title is used so that I don't have to match
     * something in the whole body of the issue.
     * @param service     Issue-service of the GitHub lib that lets you access all issues
     * @param repo        The repository that should be used
     * @return The duplicate if one is found or null
     */
    private fun findFirstDuplicate(uniqueTitle: String, service: IssueService, repo: RepositoryId): Issue? {
        val pages = service.pageIssues(repo, hashMapOf(IssueService.FILTER_STATE to IssueService.STATE_OPEN))
        return pages.flatten().firstOrNull { it.title == uniqueTitle }
    }

    /**
     * Turns collected information of an error into a new (offline) GitHub issue
     *
     * @param errorReportInformation A map of the information. Note that I remove items from there when they should not go in the issue
     * body as well. When creating the body, all remaining items are iterated.
     * @return The new issue
     */
    private fun createNewGibHubIssue(errorReportInformation: ErrorReport): Issue {
        val errorMessage = errorReportInformation.errorInfo.errorMessage.takeUnless { it.isNullOrEmpty() } ?: "Unspecified error"
        val errorHash: String = errorReportInformation.errorInfo.errorHash

        val bugLabel = Label().apply {
            name = ISSUE_LABEL_BUG
        }
        val autoGeneratedLabel = Label().apply {
            name = ISSUE_LABEL_AUTO_GENERATED
        }

        return Issue().apply {
            title = String.format(GIT_ISSUE_TITLE, errorHash, errorMessage)
            body = generateGitHubIssueBody(errorReportInformation, true)
            labels = listOf(autoGeneratedLabel, bugLabel)
        }
    }

    /**
     * Creates the body of the GitHub issue. It will contain information about the system, error report information
     * provided by the user and the full stack trace. Everything is formatted using markdown.
     *
     * @param errorReportInformation Details provided by [ErrorReport]
     * @return A markdown string representing the GitHub issue body.
     */
    private fun generateGitHubIssueBody(errorReportInformation: ErrorReport, addStacktrace: Boolean): String {
        val error = errorReportInformation.errorInfo.errorDescription ?: ""
        val stackTrace = errorReportInformation.errorInfo.errorStacktrace.takeUnless { it.isNullOrEmpty() } ?: "invalid stacktrace"

        return buildString {
            if (error.isNotEmpty()) {
                append(error)
                append("\n\n----------------------\n\n")
            }

            for ((description, value) in errorReportInformation.userInfo.asMap()) {
                append("- ")
                append(description)
                append(": ")
                append(value)
                append("\n")
            }

            if (addStacktrace) {
                append("\n```\n")
                append(stackTrace)
                append("\n```\n")
            }
        }
    }
}
