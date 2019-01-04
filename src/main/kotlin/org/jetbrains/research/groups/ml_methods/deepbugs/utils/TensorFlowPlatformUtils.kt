package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.SystemInfo
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object TensorFlowPlatformUtils {
    private val libsRoot = Paths.get(PathManager.getPluginsPath(), "DeepBugsPlugin", "bundlers").toString()
    private val winDlls = arrayOf("vcruntime140.dll", "msvcp140.dll", "concrt140.dll")

    private fun getResourceURI(dll: String) = "/bundlers/$dll"

    private fun showErrorNotification() {
        Notifications.Bus.notify(Notification(DeepBugsPluginBundle.message("error.notification.group.id"),
                DeepBugsPluginBundle.message("notification.title"),
                DeepBugsPluginBundle.message("incompatible.version.notification.message"), NotificationType.ERROR))
    }

    private fun loadLib(name: String) {
        val dllPath = Paths.get(libsRoot, name)
        if (!Files.exists(dllPath)) {
            val input = TensorFlowPlatformUtils::class.java.classLoader.getResourceAsStream(getResourceURI(name))
            val dll = File(dllPath.toString())
            val output = FileUtils.openOutputStream(dll)
            IOUtils.copy(input, output)
            input.close()
            output.close()
        }
        System.load(dllPath.toString())
    }

    fun loadLibs() {
        if (!SystemInfo.isWindows)
            return
        //TensorFlow Java API is currently available only for 64-bit systems
        if (SystemInfo.is32Bit) {
            showErrorNotification()
            return
        }
        File(libsRoot).mkdirs()
        winDlls.forEach { dll -> loadLib(dll) }
    }
}