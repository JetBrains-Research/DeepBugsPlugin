package org.jetbrains.research.deepbugs.services.errors.beans

import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.PermanentInstallationID
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.util.SystemInfo

data class ErrorReport(
    val userInfo: UserInformation,
    val errorInfo: ErrorInformation
) {
    companion object {
        fun getUsersInformation(
            error: GitHubErrorBean,
            appInfo: ApplicationInfoEx,
            namesInfo: ApplicationNamesInfo
        ): ErrorReport {
            val userInfo = UserInformation(
                error.pluginName,
                error.pluginVersion,
                SystemInfo.OS_NAME,
                SystemInfo.JAVA_VERSION,
                SystemInfo.JAVA_VENDOR,
                namesInfo.productName,
                namesInfo.fullProductName,
                appInfo.versionName,
                appInfo.isEAP.toString(),
                appInfo.build.asString(),
                appInfo.fullVersion,
                error.lastAction,
                PermanentInstallationID.get()
            )

            val errorInfo = ErrorInformation(
                error.message,
                error.stackTrace,
                error.exceptionHash,
                error.description
            )

            for (attachment in error.attachments) {
                errorInfo.attachmentName = attachment.name
                errorInfo.attachmentValue = attachment.encodedBytes
            }

            return ErrorReport(userInfo, errorInfo)
        }
    }
}
