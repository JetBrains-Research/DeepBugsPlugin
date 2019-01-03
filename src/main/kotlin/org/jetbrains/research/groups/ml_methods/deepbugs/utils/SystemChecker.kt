package org.jetbrains.research.groups.ml_methods.deepbugs.utils

import java.nio.file.Paths

object SystemChecker {
    private val winDlls = arrayOf("vcruntime140.dll", "msvcp140.dll", "concrt140.dll")

    private fun isWindowsOs(): Boolean {
        val os = System.getProperties().getProperty("os.name")
        return (os != null && os.toUpperCase().indexOf("WINDOWS") > -1)
    }

    private fun is64Bit(): Boolean =
            System.getProperties().getProperty("os.arch").endsWith("64")

    fun loadLibs() {
        if (!isWindowsOs())
            return
        var libsPath = Paths.get("bundlers", "windows-x86").toString()
        if (is64Bit())
            libsPath += "_64"
        winDlls.forEach { dll ->
            val dllPath = Paths.get(libsPath, dll).toString()
            val lib = SystemChecker::class.java.classLoader.getResource(dllPath).path.removePrefix("file:")
            System.load(lib)
        }
    }
}