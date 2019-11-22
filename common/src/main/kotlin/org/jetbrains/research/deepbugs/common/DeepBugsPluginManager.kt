package org.jetbrains.research.deepbugs.common

import com.intellij.ide.plugins.PluginManager

object DeepBugsPluginManager {
    private fun getClassLoader() = this::class.java.classLoader

    fun getPlugin() = PluginManager.getLoadedPlugins().first { it.pluginClassLoader == getClassLoader() }

    fun getPluginName() = getPlugin().name
}
