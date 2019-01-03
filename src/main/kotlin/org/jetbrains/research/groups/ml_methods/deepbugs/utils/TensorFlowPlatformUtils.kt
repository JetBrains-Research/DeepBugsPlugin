package org.jetbrains.research.groups.ml_methods.deepbugs.utils

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
    private val tfVersion = when (SystemInfo.is32Bit) {
                                true -> "x86"
                                false -> "x86_64"
                            }

    private val libsPath = Paths.get(libsRoot, "windows-$tfVersion").toString()
    private fun getResourceURI(dll: String) = "/bundlers/windows-$tfVersion/$dll"

    private fun loadLib(name: String) {
        val dllPath = Paths.get(libsPath, name)
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
        File(libsPath).mkdirs()
        winDlls.forEach { dll -> loadLib(dll) }
    }
}