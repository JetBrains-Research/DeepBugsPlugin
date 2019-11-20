package org.jetbrains.research.deepbugs.common.utils.platform

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.SystemInfo
import org.jetbrains.research.deepbugs.common.CommonResourceBundle
import tanvd.kex.Resources
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

class PlatformManager(klass: KClass<*>) {
    private val pluginId = PluginManager.getPluginByClassName(klass.java.name)
    private val winDLLs = listOf("vcruntime140.dll", "msvcp140.dll", "concrt140.dll")
    private val libsRoot = Paths.get(PathManager.getPluginsPath(), getPluginName(), "bundlers").toFile()

    private fun getPluginName() = PluginManager.getPlugin(pluginId)?.name ?: throw PlatformException("Unable to get plugin name")

    private fun loadLib(dllPath: File, name: String) {
        val libPath = File(dllPath, name)
        if (!libPath.exists()) {
            Resources.getStream("/bundlers/$name").use {
                Files.copy(it, libPath.toPath())
            }
        }

        try {
            System.load(libPath.canonicalPath)
        } catch (ex: UnsatisfiedLinkError) {
            //if library is already loaded
        }
    }

    fun checkPlatform() {
        //TensorFlow Java API is currently available only for 64-bit systems
        if (!SystemInfo.is64Bit) throw PlatformException(CommonResourceBundle.message("platform.exception.message", getPluginName()))
        if (!SystemInfo.isWindows) return

        libsRoot.mkdirs()
        for (dll in winDLLs) {
            loadLib(libsRoot, dll)
        }
    }

    companion object {
        inline fun <reified T> checkPlatformAndDependencies() = PlatformManager(T::class).checkPlatform()
    }
}


