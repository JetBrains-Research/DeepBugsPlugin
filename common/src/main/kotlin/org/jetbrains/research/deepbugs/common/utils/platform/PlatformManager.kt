package org.jetbrains.research.deepbugs.common.utils.platform

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.SystemInfo
import org.jetbrains.research.deepbugs.common.utils.DeepBugsServicesBundle
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

class PlatformManager(klass: KClass<*>) {
    private val pluginId = PluginManager.getPluginByClassName(klass.java.name)
    private val winDLLs = arrayOf("vcruntime140.dll", "msvcp140.dll", "concrt140.dll")
    private val libsRoot = Paths.get(PathManager.getPluginsPath(), getPluginName(), "bundlers").toString()

    private fun getResourceURI(dll: String) = "/bundlers/$dll"

    private fun getPluginName() = PluginManager.getPlugin(pluginId)?.name ?: throw PlatformException("Unable to get plugin name")

    private fun loadLib(dllPath: String, name: String) {
        val libPath = Paths.get(dllPath, name)
        if (!Files.exists(libPath)) {
            val input = PlatformManager::class.java.classLoader.getResourceAsStream(getResourceURI(name))!!
            Files.copy(input, libPath)
            input.close()
        }
        try {
            System.load(libPath.toString())
        } catch (ex: UnsatisfiedLinkError) {
            //if library is already loaded
        }
    }

    fun checkPlatform() {
        //TensorFlow Java API is currently available only for 64-bit systems
        if (!SystemInfo.is64Bit) throw PlatformException(DeepBugsServicesBundle.message("platform.exception.message", getPluginName()))
        if (!SystemInfo.isWindows) return

        File(libsRoot).mkdirs()
        winDLLs.forEach { dll -> loadLib(libsRoot, dll) }
    }

    companion object {
        inline fun <reified T> checkPlatformAndDependencies() = PlatformManager(T::class).checkPlatform()
    }
}


