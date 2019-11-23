package org.jetbrains.research.deepbugs.common.utils.errors.beans

data class UserInformation(
    val pluginName: String?,
    val pluginVersion: String?,
    val OSName: String,
    val javaVersion: String,
    val javaVMVendor: String,
    val appName: String,
    val appFullName: String,
    val appVersionName: String,
    val isEap: String,
    val appBuild: String,
    val appVersion: String,
    val lastEventType: String,
    val permanentInstallationID: String
) {
    fun asMap() = mapOf(
        "Plugin Name" to pluginName,
        "Plugin Version" to pluginVersion,
        "OS Name" to OSName,
        "Java Version" to javaVersion,
        "Java VM Vendor" to javaVMVendor,
        "App Name" to appName,
        "App Full Name" to appFullName,
        "App Version Name" to appVersionName,
        "Is EAP" to isEap,
        "App Build" to appBuild,
        "App Version" to appVersion,
        "Last EventType" to lastEventType,
        "User's Permanent Installation ID" to permanentInstallationID
    )
}
