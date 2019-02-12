package org.jetbrains.research.groups.ml_methods.deepbugs.services.utils

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.SystemInfo
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class PlatformManager<T> private constructor(klass: Class<T>) {
    private val pluginId = PluginManager.getPluginByClassName(klass.name)
    private val winDlls = arrayOf("vcruntime140.dll", "msvcp140.dll", "concrt140.dll")
    private val libsRoot = Paths.get(PathManager.getPluginsPath(), getPluginName(), "bundlers").toString()

    private fun getResourceURI(dll: String) = "/bundlers/$dll"

    private fun getPluginName() = PluginManager.getPlugin(pluginId)?.name ?: throw PlatformException("Unable to get plugin name")

    private fun loadLib(dllPath: String, name: String) {
        val libPath = Paths.get(dllPath, name)
        if (!Files.exists(libPath)) {
            val input = PlatformManager::class.java.classLoader.getResourceAsStream(getResourceURI(name))
            val dll = File(libPath.toString())
            val output = FileUtils.openOutputStream(dll)
            IOUtils.copy(input, output)
            input.close()
            output.close()
        }
        System.load(libPath.toString())
    }

    fun checkPlatform() {
        //TensorFlow Java API is currently available only for 64-bit systems
        if (!SystemInfo.is64Bit)
            throw PlatformException(DeepBugsServicesBundle.message("platform.exception.message", getPluginName()))
        if (!SystemInfo.isWindows)
            return
        File(libsRoot).mkdirs()
        winDlls.forEach { dll -> loadLib(libsRoot, dll) }
    }

    companion object {
        fun <T> checkPlatformAndDependencies(klass: Class<T>) = PlatformManager(klass).checkPlatform()
    }

}

class PlatformException(message: String) : Exception(message)