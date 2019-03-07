package org.jetbrains.research.groups.ml_methods.deepbugs.services.error_reporting

import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.PermanentInstallationID
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.util.SystemInfo
import org.jetbrains.research.groups.ml_methods.deepbugs.services.error_reporting.ErrorReportInformation.InformationType.*
import java.util.*

/**
 * Collects information about the running IDEA and the error
 */
internal class ErrorReportInformation private constructor(
        error: GitHubErrorBean,
        appInfo: ApplicationInfoEx,
        namesInfo: ApplicationNamesInfo
) {

    private val information = EnumMap<ErrorReportInformation.InformationType, String>(ErrorReportInformation.InformationType::class.java)

    enum class InformationType {
        ERROR_DESCRIPTION, PLUGIN_NAME, PLUGIN_VERSION, OS_NAME, JAVA_VERSION, JAVA_VM_VENDOR,
        APP_NAME, APP_FULL_NAME, APP_VERSION_NAME, IS_EAP, APP_BUILD, APP_VERSION, LAST_ACTION,
        PERMANENT_INSTALLATION_ID, ERROR_MESSAGE, ERROR_STACKTRACE, ERROR_HASH, ATTACHMENT_NAME, ATTACHMENT_VALUE
    }

    init {
        information[ERROR_DESCRIPTION] = error.description

        information[PLUGIN_NAME] = error.pluginName
        information[PLUGIN_VERSION] = error.pluginVersion
        information[OS_NAME] = SystemInfo.OS_NAME
        information[JAVA_VERSION] = SystemInfo.JAVA_VERSION
        information[JAVA_VM_VENDOR] = SystemInfo.JAVA_VENDOR
        information[APP_NAME] = namesInfo.productName
        information[APP_FULL_NAME] = namesInfo.fullProductName
        information[APP_VERSION_NAME] = appInfo.versionName
        information[IS_EAP] = java.lang.Boolean.toString(appInfo.isEAP)
        information[APP_BUILD] = appInfo.build.asString()
        information[APP_VERSION] = appInfo.fullVersion
        information[PERMANENT_INSTALLATION_ID] = PermanentInstallationID.get()
        information[LAST_ACTION] = error.lastAction
        information[ERROR_MESSAGE] = error.message
        information[ERROR_STACKTRACE] = error.stackTrace
        information[ERROR_HASH] = error.exceptionHash

        for (attachment in error.attachments) {
            information[ATTACHMENT_NAME] = attachment.name
            information[ATTACHMENT_VALUE] = attachment.encodedBytes
        }

    }

    operator fun get(informationType: ErrorReportInformation.InformationType): String? {
        return information[informationType]
    }

    companion object {

        fun getUsersInformation(
                error: GitHubErrorBean,
                appInfo: ApplicationInfoEx,
                namesInfo: ApplicationNamesInfo
        ): ErrorReportInformation {
            return ErrorReportInformation(error, appInfo, namesInfo)
        }
    }
}