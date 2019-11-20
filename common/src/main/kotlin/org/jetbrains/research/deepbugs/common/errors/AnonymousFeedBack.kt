package org.jetbrains.research.deepbugs.common.errors

import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus
import org.eclipse.egit.github.core.*
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.IssueService
import org.jetbrains.research.deepbugs.common.errors.beans.ErrorReport
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import java.util.*

/**
 * Provides functionality to create and send GitHub issues when an exception is thrown by a plugin.
 */
internal object AnonymousFeedback {
    private const val TOKEN_FILE = "errorReporterToken.dms"
    private const val GIT_REPO_USER = "JetBrains-Research"
    private const val GIT_REPO = "DeepBugsPlugin"
    private const val ISSUE_LABEL_BUG = "bug"
    private const val ISSUE_LABEL_AUTO_GENERATED = "auto-generated"
    private const val GIT_ISSUE_TITLE = "[auto-generated:%s] %s"
    private const val HTML_URL_TO_CREATE_NEW_ISSUE = "https://github.com/JetBrains-Research/DeepBugsPlugin/issues/new"

    /**
     * Makes a connection to GitHub. Checks if there is an issue that is a duplicate and based on this, creates either a
     * new issue or comments on the duplicate (if the user provided additional information).
     *
     * @param errorReportInformation Information collected by [ErrorReport]
     * @return The report info that is then used in [GitHubErrorReporter] to show the user a balloon with the link
     * of the created issue.
     */
    fun sendFeedback(errorReportInformation: ErrorReport): SubmittedReportInfo {

        val result: SubmittedReportInfo
        try {
            val gitAccessToken = GitHubAccessTokenScrambler.decrypt(AnonymousFeedback::class.java.classLoader.getResourceAsStream(TOKEN_FILE))

            val client = GitHubClient()
            client.setOAuth2Token(gitAccessToken)
            val repoID = RepositoryId(GIT_REPO_USER, GIT_REPO)
            val issueService = IssueService(client)

            var newGibHubIssue = createNewGibHubIssue(errorReportInformation)
            val duplicate = findFirstDuplicate(newGibHubIssue.title, issueService, repoID)
            var isNewIssue = true
            if (duplicate != null) {
                val newErrorComment = generateGitHubIssueBody(errorReportInformation, false)
                issueService.createComment(repoID, duplicate.number, newErrorComment)
                newGibHubIssue = duplicate
                isNewIssue = false
            } else {
                newGibHubIssue = issueService.createIssue(repoID, newGibHubIssue)
            }

            val id = newGibHubIssue.number
            val htmlUrl = newGibHubIssue.htmlUrl
            val message = CommonResourceBundle.message(if (isNewIssue) "git.issue.text" else "git.issue.duplicate.text", htmlUrl, id)
            result = SubmittedReportInfo(htmlUrl, message, if (isNewIssue) SubmissionStatus.NEW_ISSUE else SubmissionStatus.DUPLICATE)
            return result
        } catch (e: Exception) {
            return SubmittedReportInfo(HTML_URL_TO_CREATE_NEW_ISSUE,
                CommonResourceBundle.message("report.error.connection.failure", HTML_URL_TO_CREATE_NEW_ISSUE),
                SubmissionStatus.FAILED)
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
        val searchParameters = HashMap<String, String>(2)
        searchParameters[IssueService.FILTER_STATE] = IssueService.STATE_OPEN
        val pages = service.pageIssues(repo, searchParameters)
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
        var errorMessage: String? = errorReportInformation.errorInfo.errorMessage
        if (errorMessage.isNullOrEmpty()) {
            errorMessage = "Unspecified error"
        }
        val errorHash: String = errorReportInformation.errorInfo.errorHash

        val gitHubIssue = Issue()
        val body = generateGitHubIssueBody(errorReportInformation, true)
        gitHubIssue.title = String.format(GIT_ISSUE_TITLE, errorHash, errorMessage)
        gitHubIssue.body = body
        val bugLabel = Label()
        bugLabel.name = ISSUE_LABEL_BUG
        val autoGeneratedLabel = Label()
        autoGeneratedLabel.name = ISSUE_LABEL_AUTO_GENERATED
        gitHubIssue.labels = listOf(autoGeneratedLabel, bugLabel)
        return gitHubIssue
    }

    /**
     * Creates the body of the GitHub issue. It will contain information about the system, error report information
     * provided by the user and the full stack trace. Everything is formatted using markdown.
     *
     * @param errorReportInformation Details provided by [ErrorReport]
     * @return A markdown string representing the GitHub issue body.
     */
    private fun generateGitHubIssueBody(errorReportInformation: ErrorReport, addStacktrace: Boolean): String {
        val errorDescription: String = errorReportInformation.errorInfo.errorDescription ?: ""

        var stackTrace: String? = errorReportInformation.errorInfo.errorStacktrace
        if (stackTrace.isNullOrEmpty()) {
            stackTrace = "invalid stacktrace"
        }

        return buildString {
            if (errorDescription.isNotEmpty()) {
                append(errorDescription)
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
