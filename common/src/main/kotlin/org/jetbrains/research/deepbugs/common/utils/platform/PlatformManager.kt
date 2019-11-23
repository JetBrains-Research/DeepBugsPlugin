package org.jetbrains.research.deepbugs.common.utils.platform

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.SystemInfo
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import org.jetbrains.research.deepbugs.common.DeepBugsPlugin
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object PlatformManager {
    private val pluginName
        get() = DeepBugsPlugin.descriptor.name ?: throw PlatformException("Unable to get plugin name")

    private val winDLLs = listOf("vcruntime140.dll", "msvcp140.dll", "concrt140.dll")
    private val libsRoot = Paths.get(PathManager.getPluginsPath(), pluginName, "bundlers").toFile()


    private fun loadLib(name: String) {
        val libPath = File(libsRoot, name)
        if (!libPath.exists()) {
            PlatformManager::class.java.classLoader.getResourceAsStream("/bundlers/$name")!!.use {
                Files.copy(it, libPath.toPath())
            }
        }

        try {
            System.load(libPath.canonicalPath)
        } catch (ex: UnsatisfiedLinkError) {
            //if library is already loaded
        }
    }

    fun checkPlatformAndDependencies() {
        //TensorFlow Java API is currently available only for 64-bit systems
        if (!SystemInfo.is64Bit) throw PlatformException(CommonResourceBundle.message("platform.exception.message", pluginName))
        if (!SystemInfo.isWindows) return

        libsRoot.mkdirs()
        for (dll in winDLLs) {
            loadLib(dll)
        }
    }
}
