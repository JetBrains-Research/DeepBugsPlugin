package org.jetbrains.research.deepbugs.common

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager

object DeepBugsPlugin {
    private val classLoader: ClassLoader
        get() = this::class.java.classLoader

    val plugin: IdeaPluginDescriptor
        get() = PluginManager.getLoadedPlugins().single { it.pluginClassLoader == classLoader }

    val pluginName: String
        get() = plugin.name
}
